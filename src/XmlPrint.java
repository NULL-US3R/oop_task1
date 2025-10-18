import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class XmlPrint {
    public static class Tag{
        String str;
        public int type; //0-open, 1-close, 2-self-close, 3 - <??>
        public Tag(String str, int type){
            this.str=str;
            this.type=type;
        }
        void print(){
            if(type==0 || type==3){
                System.out.print("<");
                System.out.print(str);
                System.out.println(">");
            }
            if(type==1){
                System.out.print("</");
                System.out.print(str);
                System.out.println(">");
            }
            if(type==2){
                System.out.print("<");
                System.out.print(str);
                System.out.println("/>");
            }
        }
    }
    public static class XmlFile{
        List<Tag> tags;
        List<String> strings;
        public XmlFile(Path adr){
            strings = new ArrayList<>();
            tags = new ArrayList<>();

            String str;
            try {
                str = Files.readString(adr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // System.out.print(str);

            fromString(str);
        }

        public XmlFile(String str){
            strings = new ArrayList<>();
            tags = new ArrayList<>();
            //System.out.print(str);
            fromString(str);
        }

        void fromString(String str){
            boolean isTag=false;
            String cur = "";
            int tagtype=0;
            int chars=0;
            for(int i=0; i<str.length(); i++){
                if(isTag){
                    if(str.charAt(i) == '/' && str.charAt(i+1) == '>') {
                        tagtype = 2;
                        continue;
                    } else if(str.charAt(i) == '>'){
                        tags.add(new Tag(cur,tagtype));
                        cur = "";
                        isTag=false;
                        chars=0;
                    }else{
                        cur = cur + str.charAt(i);
                    }
                }else{
                    if(str.charAt(i) == '<'){
                        if(str.charAt(i+1) == '/'){
                            tagtype=1;
                            i++;
                        } else if(str.charAt(i+1) == '?'){
                            tagtype=3;
                        }else{
                            tagtype=0;
                        }
                        cur = cur.trim();
                        strings.add(cur);
                        cur = "";
                        isTag=true;
                    }else{
                        if(str.charAt(i)==' ' && chars>20){
                            cur = cur + '\n';
                            chars=0;
                        }else {
                            cur = cur + str.charAt(i);
                            chars++;
                        }
                    }
                }
            }
        }

        public void writeToFile(Path adr){
            PrintStream origOut = System.out;
            try{
                System.setOut(new PrintStream(new File(adr.toUri())));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            print();
            System.setOut(origOut);
        }
        void print(){
            String tabSymb = "    ";
            int tc = 0;
            for(int i=0; i<strings.toArray().length; i++){

                String tab = "";
                for(int j=0; j<tc; j++){
                    tab += tabSymb;
                }

                if(!strings.get(i).isEmpty()) {
                    System.out.print(tab);
                    System.out.println(strings.get(i).replace("\n", "\n"+tab));
                }

                if(tags.get(i).type==1)tc--;
                tab = "";
                for(int j=0; j<tc; j++){
                    tab += tabSymb;
                }

                //System.out.print(tc);
                System.out.print(tab);
                tags.get(i).print();
                if(tags.get(i).type==0)tc++;

            }
        }
    }
}
