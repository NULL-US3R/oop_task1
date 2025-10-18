void main() {
    XmlPrint.XmlFile f = new XmlPrint.XmlFile(Path.of("/home/main/IdeaProjects/oop1/tst.xml"));
    f.print();
    f.writeToFile(Path.of("/home/main/IdeaProjects/oop1/out.xml"));
    System.out.println();
    XmlPrint.XmlFile f1 = new XmlPrint.XmlFile("<?xml text?><xml>some very very long text to test character limitations of automatic enters<tag1>contents of tag</tag1></xml>");
    f1.print();
    f1.writeToFile(Path.of("/home/main/IdeaProjects/oop1/out1.xml"));
}
