package mobi.stos.projetoestacio.util;

import java.net.URLEncoder;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class GeoLocation {

    public enum TypeAddress {

        COMPLETE, NUMBER, ADDRESS, NEIGHBOR, CITY, STATE, COUNTRY, CEP, LATLON, LAT, LON;
    }

    public Map<TypeAddress, Double> encodeLatLon(String address) {
        Map<TypeAddress, Double> map = new HashMap<>();
        try {
            String uri = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF8") + "&sensor=false";
            String url = (uri);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(url);
            Element channel = doc.getRootElement().getChild("result");

            Element geometry = channel.getChild("geometry");
            Element location = geometry.getChild("location");
            map.put(TypeAddress.LAT, Double.parseDouble(location.getChildText("lat")));
            map.put(TypeAddress.LON, Double.parseDouble(location.getChildText("lng")));

        } catch (Exception e) {
            System.out.println("Erro ao tentar obter o endereço." + this.getClass().getName());
            e.printStackTrace();
        } finally {
            return map;
        }
    }

    public Map<TypeAddress, String> decodeLatLon(double latitude, double longitude) {
        Map<TypeAddress, String> map = new EnumMap<>(TypeAddress.class);
        map.put(TypeAddress.LATLON, latitude + "," + longitude);
        try {
            String uri = "http://maps.googleapis.com/maps/api/geocode/xml?latlng=" + latitude + "," + longitude + "&sensor=false";
            String url = (uri);
            System.out.println(url);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(url);
            Element channel = doc.getRootElement().getChild("result");

            map.put(TypeAddress.COMPLETE, channel.getChildText("formatted_address"));

            Iterator it = channel.getChildren("address_component").iterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                String s = element.getChildText("short_name");
                List<Element> eles = element.getChildren("type");
                for (Element e : eles) {
                    switch (e.getText()) {
                        case "route":
                            map.put(TypeAddress.ADDRESS, s);
                            break;
                        case "neighborhood":
                        case "sublocality":
                            map.put(TypeAddress.NEIGHBOR, s);
                            break;
                        case "locality":
                            map.put(TypeAddress.CITY, s);
                            break;
                        case "administrative_area_level_1":
                            map.put(TypeAddress.STATE, s);
                            break;
                        case "country":
                            map.put(TypeAddress.COUNTRY, s);
                            break;
                        case "street_number":
                            map.put(TypeAddress.NUMBER, s);
                            break;
                        case "postal_code":
                            map.put(TypeAddress.CEP, s);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao tentar obter o endereço." + this.getClass().getName());
            e.printStackTrace();
        } finally {
            return map;
        }
    }
}
