package library.timothy.Resources;

public class UriResources {

    public static class Server {
        private static final String HOST = "http://jasonchi.ddns.net:8080/";
        private static final String API = HOST + "api/";
        public static final String Product = API + "Product";
        public static final String LogIn = API + "Login";
        public static final String Combo = API + "Combo";
        public static final String PushNotification = API + "PushNotification";
    }

    public static class Test{
        private static final String KAOHSIUNG = "http://data.kaohsiung.gov.tw/Opendata/DownLoad.aspx";
        public static final String OpenData1 = KAOHSIUNG + "?Type=2&CaseNo1=BA&CaseNo2=1&FileType=2&Lang=C&FolderType=O";
        public static final String OpenData2 = KAOHSIUNG + "?Type=2&CaseNo1=AM&CaseNo2=5&FileType=1&Lang=C&FolderType=O";
        public static final String OpenData3 = KAOHSIUNG + "?Type=2&CaseNo1=AF&CaseNo2=2&FileType=2&Lang=C&FolderType=O";
        public static final String NetImageTest = "http://i.imgur.com/7spzG.png";
    }
}
