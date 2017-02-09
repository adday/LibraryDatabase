import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lab4_xml {
	
	public ArrayList<Borrowed_by> readXML(String fileName)
	{
		ArrayList<Borrowed_by> records = new ArrayList<Borrowed_by>();
		DateFormat xmlDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			
			File file = new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Borrowed_by");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Borrowed_by record = new Borrowed_by();
					
					Element sectionNode = (Element) fstNode;
					
					NodeList memberIdElementList = sectionNode.getElementsByTagName("MemberID");
					Element memberIdElmnt = (Element) memberIdElementList.item(0);
					NodeList memberIdNodeList = memberIdElmnt.getChildNodes();
					record.set_member_id(Integer.parseInt(((Node) memberIdNodeList.item(0)).getNodeValue().trim()));

					NodeList secnoElementList = sectionNode.getElementsByTagName("ISBN");
					Element secnoElmnt = (Element) secnoElementList.item(0);
					NodeList secno = secnoElmnt.getChildNodes();
					String ISBN = ((Node) secno.item(0)).getNodeValue().trim();
					ISBN = ISBN.replace("-","");
					record.set_ISBN(ISBN);

					NodeList codateElementList = sectionNode.getElementsByTagName("Checkout_date");
					Element codElmnt = (Element) codateElementList.item(0);
					NodeList cod = codElmnt.getChildNodes();
					String checkout_date = ((Node) cod.item(0)).getNodeValue().trim();
					try{
						Date temp_date = xmlDateFormat.parse(checkout_date);
						checkout_date = sqlDateFormat.format(temp_date);
						record.set_checkout_date(checkout_date);
						}
					catch(Exception e){
						record.set_checkout_date("NULL");
					}

					NodeList cidateElementList = sectionNode.getElementsByTagName("Checkin_date");
					Element cidElmnt = (Element) cidateElementList.item(0);
					NodeList cid = cidElmnt.getChildNodes();
					String checkin_date = ((Node) cid.item(0)).getNodeValue().trim();
					try{
						Date temp_date = xmlDateFormat.parse(checkin_date);
						checkin_date = sqlDateFormat.format(temp_date);
						record.set_checkin_date(checkin_date);
						}
					catch(Exception e){
						record.set_checkin_date("NULL");
					}

					records.add(record);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}
	

}//end class 