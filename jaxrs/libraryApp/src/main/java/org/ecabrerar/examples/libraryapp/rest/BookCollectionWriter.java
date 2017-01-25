/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ecabrerar.examples.libraryapp.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.ecabrerar.examples.libraryapp.domain.Book;

/**
 *
 * @author ecabrerar
 * @date Sep 1, 2016
 */
@Provider
public class BookCollectionWriter implements MessageBodyWriter<List<Book>> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;

	}

	@Override
	public long getSize(List<Book> books, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(List<Book> books, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {

		if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)) {

			try (StringWriter writer = new StringWriter()) {

				try (JsonGenerator generator = Json.createGenerator(writer)) {
					HashMap<String, Object> configs = new HashMap<>(1);
					configs.put(JsonGenerator.PRETTY_PRINTING, true);

					generator.writeStartArray();

					books.stream().forEach(book -> generator.writeStartObject()
															.write("Name", book.getName())
															.write("ISBN", book.getIsbn())
															.write("Author", book.getAuthor())
															.writeEnd());


					generator.writeEnd();
				}

				entityStream.write(writer.toString().getBytes());
			}

		} else if (mediaType.equals(MediaType.TEXT_PLAIN_TYPE)) {
			StringBuilder stringBuilder = new StringBuilder("Book ");

			books.stream().forEach((Book book) -> stringBuilder.append(book.toString()).append("\n"));

			entityStream.write(stringBuilder.toString().getBytes());
		}

	}

}
