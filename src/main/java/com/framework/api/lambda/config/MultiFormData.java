package com.framework.api.lambda.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Base64.Decoder;
import java.util.Base64;

public class MultiFormData {
	Map<String, String> params;
	Map<String, MultiFormFile> files;

	public static MultiFormData parse(String base64, String boundary) {

		Base64.Decoder decoder = Base64.getDecoder();
		byte[] decodedByte = decoder.decode(base64);
		String decoded = new String(decodedByte, StandardCharsets.UTF_8);
		// System.out.println(decoded);

		Map<String, String> params = new HashMap<String, String>();
		Map<String, MultiFormFile> files = new HashMap<String, MultiFormFile>();

		MultiFormData form = new MultiFormData();
		String temp = "--" + boundary;

		String[] split = decoded.split(boundary);

		for (int i = 0; i < split.length; i++) {

			if (split[i].contains("Content-Disposition")) {
				if (split[i].contains("Content-Type")) {

					MultiFormFile file = new MultiFormFile();

					String name = find("name=\".*\";", split[i]).trim();
					name = name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\""));
					file.setName(name);

					String filename = find("filename=\".*\"", split[i]);
					filename = filename.substring(filename.indexOf("\"") + 1, filename.lastIndexOf("\""));
					file.setFilename(filename);

					String contentType = find("Content-Type:.*", split[i]);
					contentType = contentType.substring(contentType.indexOf(" ") + 1);
					file.setContextType(contentType);

					int begin = split[i].indexOf(contentType) + contentType.length();

					// file.setOutputStream(fileString.getBytes());

					files.put(name, file);

				} else {

					String name = find("name=\".*\"", split[i]).trim();
					int begin = split[i].indexOf(name) + name.length();

					name = name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\""));
					String value = split[i].substring(begin).trim();
					params.put(name, value);

				}
			}
		}

		form.setFiles(files);
		form.setParams(params);

		ByteArrayInputStream content = new ByteArrayInputStream(decodedByte);

		try {
			MultipartStream multipartStream = new MultipartStream(content, boundary.getBytes());

			boolean nextPart = multipartStream.skipPreamble();
			while (nextPart) {
				String header = multipartStream.readHeaders();
				System.out.println("");
				System.out.println("Headers:");
				System.out.println(header);
				System.out.println("Body:");
				if (header.contains("file")) {
					System.out.println("entrooo");
					multipartStream.readBodyData(new FileOutputStream(new File("archivo_cesar.pdf")));

				}
				System.out.println("");
				nextPart = multipartStream.readBoundary();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

		return form;

	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, MultiFormFile> getFiles() {
		return files;
	}

	public void setFiles(Map<String, MultiFormFile> files) {
		this.files = files;
	}

	private static String find(String regexpresion, String text) {

		Pattern pattern = Pattern.compile(regexpresion);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(0);
		}

		return null;
	}

	public static class MultiFormFile {
		private byte[] outputStream;
		private String name;
		private String filename;
		private String contextType;

		public byte[] getOutputStream() {
			return outputStream;
		}

		public void setOutputStream(byte[] outputStream) {
			this.outputStream = outputStream;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getContextType() {
			return contextType;
		}

		public void setContextType(String contextType) {
			this.contextType = contextType;
		}

	}

}
