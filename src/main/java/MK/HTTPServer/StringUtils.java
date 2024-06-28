package MK.HTTPServer;


public class StringUtils
{
    public static String[] splitStringAtPos(String target, int position)
    {
        String a = target.substring(0, position);
        String b = target.substring(position+1, target.length());

        return new String[]{a,b};
    }
}
