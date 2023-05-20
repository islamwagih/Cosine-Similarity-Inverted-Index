import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main
{

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


    public static Set<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
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
        ArrayList<Pair> scores = new ArrayList<>();
        
        for(String fileName:files)
        {
            int docId = parseDocId(fileName);
            String content = getContent("D:/Projects/InvertedIndex/Docs/"+fileName);
            HashMap<String, Integer> fileIndx = getTokens(content);
            HashMap<String, Integer> queryIndx = getTokens(query);
            double cosSim = getCosineSimilarity(fileIndx, queryIndx);
            scores.add(new Pair(cosSim, docId));
        }

        Collections.sort(scores);
        for(Pair score:scores)
        {
            System.out.println("Cosine Similarity between file "+score.getDocId()+" and query is "+score.getCosSim());
        }
    }
}
