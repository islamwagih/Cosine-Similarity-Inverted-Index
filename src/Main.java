import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main
{

    public static double dotProduct(ArrayList<Integer> a, ArrayList<Integer> b)
    {
        double result = 0.0;
        for(int i=0;i<a.size();i++)
        {
            result += a.get(i) * b.get(i);
        }
        return result;
    }

    public static double magnitude(ArrayList<Integer> a)
    {
        double result = 0.0;
        for(int i=0;i<a.size();i++)
        {
            result += a.get(i) * a.get(i);
        }
        return Math.sqrt(result);
    }

    public static double cosineSimilarity(ArrayList<Integer> a, ArrayList<Integer> b)
    {
        return dotProduct(a,b)/(magnitude(a)*magnitude(b));

    }

    public static double getCosineSimilarity(HashMap<String, Integer> file, HashMap<String, Integer> query)
    {
        HashMap<String, Integer> result = new HashMap<>();
        double numerator = 0.0;
        for(String str:query.keySet())
        {
            if(file.containsKey(str))
            {
                numerator += file.get(str) * query.get(str);
            }
        }

        double firstDeno = 0.0;
        for(Integer val:file.values())
        {
            firstDeno += val*val;
        }

        double secDeno = 0.0;
        for(Integer val:query.values())
        {
            secDeno += val*val;
        }

        firstDeno = Math.sqrt(firstDeno);
        secDeno = Math.sqrt(secDeno);

        if(firstDeno*secDeno == 0.0) return 0.0;

        return numerator/(firstDeno*secDeno);
    }


    public static String preprocess(String word)
    {
        word = word.toLowerCase(Locale.ROOT);
        String newWord = "";
        int len = word.length(), pos = 1;
        for(char ch:word.toCharArray())
        {
            if((ch >= 'a' && ch <= 'z'))
            {
                newWord += ch;
            }
            pos++;
        }
        return newWord;
    }

    public static Set<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public static ArrayList<String> findLines(String fileName) {
        String WORD_FILE = fileName;
        ArrayList<String> words = new ArrayList<>();
        try {
            File file = new File(WORD_FILE);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext() == true) {
                String word = scanner.next();
                word = preprocess(word);
                if(word.length() > 0) words.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found.");
        }
        return words;
    }

    public static String getContent(String fileName)
    {
        String WORD_FILE = fileName;
        String content = "";
        try {
            File file = new File(WORD_FILE);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext() == true) {
                content += scanner.next();
                content += " ";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found.");
        }
        return content;
    }

    public static int parseDocId(String fileName)
    {
        String strId = "";
        boolean underScore = false;
        for(char ch:fileName.toCharArray())
        {
            if(ch == '_')
            {
                underScore = true;
                continue;
            }
            if(ch == '.') break;
            if(underScore) strId += ch;
        }
        return Integer.parseInt(strId);
    }

    public static HashMap<String, Integer> getTokens(String input)
    {
        Pattern pattern = Pattern.compile("\\w+"); //remove all non alphanumeric
        Matcher matcher = pattern.matcher(input);
        HashMap<String, Integer> tokens = new HashMap<>();
        while (matcher.find())
        {
            String key = matcher.group();
            key = key.toLowerCase(Locale.ROOT);
            if(tokens.containsKey(key)) {
                tokens.put(key, tokens.get(key)+1);
            }else {
                tokens.put(key, 1);
            }
        }
        return tokens;
    }


    public static void main(String[] args)
    {
        String query = "The Tomb of the ANDRONICI appearing; the Tribunes and Senators aloft. Enter, below, from one side, SATURNINUS and his Followers; and, from the other side, BASSIANUS and his Followers; with drum and colours";
        Set<String> files = listFiles("Docs");
        for(String fileName:files)
        {
            int docId = parseDocId(fileName);
            String content = getContent("D:/Projects/InvertedIndex/Docs/"+fileName);
            HashMap<String, Integer> fileIndx = getTokens(content);
            HashMap<String, Integer> queryIndx = getTokens(query);
            System.out.println("Cosine Similarity between file "+docId+" and query is "+getCosineSimilarity(fileIndx, queryIndx));
        }
/*
        try (PrintWriter printer = new PrintWriter(new FileOutputStream("InvertedIndexAnalytics.txt", false))) {
            for (String key : invertedIndex.keySet())
            {
                printer.println("----------------------------");
                printer.println("key: "+key);
                for(Pair pair:invertedIndex.get(key).getDocsIdAndFrq())
                {
                    printer.println("Doc id: "+pair.getDocId()+ ", Frq: "+pair.getDocFrq());
                }
                printer.println("----------------------------");
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
*/

    }
}
