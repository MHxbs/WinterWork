import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnction {
        private final String Driver ="com.mysql.cj.jdbc.Driver";
        private final String URL="jdbc:mysql://localhost:3306/javademo?useSSL=true&serverTimezone=UTC";
        private final String USER = "root";
        private final String PASSWORD = "";
        private Connection conn=null;

        public DBConnction(){
            try {
                Class.forName(Driver);
                this.conn= DriverManager.getConnection(URL,USER,PASSWORD);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("failure!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("F");

            }
        }

    public Connection getConnction(){

        return conn;
    }

    public void close(){
        if (this.conn!=null){
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("数据库链接失败！");
            }
        }
    }
}
