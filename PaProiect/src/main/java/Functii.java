import java.io.PrintStream;
import java.sql.*;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class Functii {
    public int validare(String cuvant,Connection connection) {
        PreparedStatement statement;
        String getGenre="select * from autocomplete WHERE formNoAccent = ? ";
        int i=0;
        try{
            statement = connection.prepareStatement(getGenre);
            statement.setString(1,cuvant);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ++i;
            }

        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return i;
    }

    public static List<String> cautare(String cuvant,Connection connection) throws SQLException {

        List <String>result=new ArrayList<>();
        cuvant=cuvant+"%";
        PreparedStatement statement;
        String getGenre="select * from autocomplete WHERE formNoAccent LIKE ?";
        int i=0;

        statement = connection.prepareStatement(getGenre);
        statement.setString(1,cuvant);
        ResultSet rs = statement.executeQuery();

     while(rs.next())
     if(rs.getString(1).length()>=3)
          result.add(rs.getString(1));
     if(result.size()!=0)
         return result;
     return null;
        }
        }
