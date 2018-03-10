import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Comment {

    private  String comment;
    private String username;
    private String tousername;
    private Timestamp calendar ;
    private int id;
    private int fid;
    private int videoID;

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = Integer.parseInt(videoID);
    }

    public Timestamp getCalendar() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return calendar;
    }

    public void setCalendar(Timestamp calendar) {
        this.calendar =calendar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }


    public String getTousername() {
        return tousername;
    }

    public void setTousername(String tousername) {
        this.tousername = tousername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment= comment;
    }

   public static String display(int ID){
        String total="";
       Connection con=new DBConnction().getConnction();

       String sql="SELECT * FROM comments where Fid = ? ";
       try {
           PreparedStatement pst=con.prepareStatement(sql);
           pst.setInt(1,ID);
           ResultSet resultSet=pst.executeQuery();
           while(resultSet.next()){
                String comment=resultSet.getString("comment");
                String username=resultSet.getString("username");
                String tousername=resultSet.getString("tousername");
                String calendar= String.valueOf(resultSet.getTimestamp("calendar"));
                int id=Integer.parseInt(resultSet.getString("id"));
                int Fid=Integer.parseInt(resultSet.getString("Fid"));

                String begin= "\"comment\":\""+comment+"\","+
                        "\"username \":\""+username+"\","+
                        "\"tousername \":\""+tousername+"\","+
                        "\" calendar\":\""+calendar+"\","+
                        "\"id\":"+id+","+
                        "\"Fid \":"+Fid+","+
                        "\"commentSon\": [";
                String commentSon=display(id);
                String end="]";
                String all=begin+commentSon+end;

                total+=all;
           }
            return total;
       } catch (SQLException e) {
           e.printStackTrace();
       }

        return "";
   }
}
