import javax.servlet.http.HttpSession;

public class LotteryNumber {
    private String luckyNumbers;
    private DatabaseConnection db = new DatabaseConnection();

    public void createLatestLuckyNumbers(HttpSession session){
        if(session.getAttribute("username") == null && session.getAttribute("drawCreated") == null) {
            String number = "";
            for (int i = 0; i < 6; i++) {
                int randomNumber = (int) ((Math.random() * ((60 - 0) + 1)) + 0);
                number += i != 5 ? String.valueOf(randomNumber) + "," : String.valueOf(randomNumber);
            }
            String query = String.format("INSERT INTO RandomLotteryDraw(Numbers,TimeCreated) VALUES('%s',NOW())",number);
            this.db.runQuery(query);
            session.setAttribute("drawCreated",true);
         }
    }

    public String getLuckyNumbers() {
        return luckyNumbers;
    }



}
