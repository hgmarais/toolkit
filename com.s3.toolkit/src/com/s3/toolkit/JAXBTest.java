package com.s3.toolkit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class JAXBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConnectivityServer cs = new ConnectivityServer();

    // create JAXB context and instantiate marshaller
    JAXBContext context = JAXBContext.newInstance(ConnectivityServer.class);
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    // Write to System.out
    m.marshal(cs, System.out);

//    ByteArrayOutputStream os = new ByteArrayOutputStream();
//    // Write to File
//    m.marshal(bookstore, os);
//    
//    System.out.println("Marchal : ");
//    System.out.println(new String(os.toByteArray()));
//
//    // get variables from our xml file, created before
//    System.out.println();
//    System.out.println("Output from our XML File: ");
//    Unmarshaller um = context.createUnmarshaller();
//    Bookstore bookstore2 = (Bookstore) um.unmarshal(new ByteArrayInputStream(os.toByteArray()));
//    ArrayList<Book> list = bookstore2.getBooksList();
//    for (Book book : list) {
//      System.out.println("Book: " + book.getName() + " from "
//          + book.getAuthor());
//    }
	}

}
