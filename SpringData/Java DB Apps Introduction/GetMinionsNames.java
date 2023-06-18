import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GetMinionsNames {
    private static final String COLUMN_LABEL_NAME = "name";
    private static final String COLUMN_LABEL_AGE = "age";
    private static final String GET_MINIONS_NAME_AGE_BY_VILLAIN_ID =
            "select  m.name,m.age  from minions as m" +
                    " join minions_villains mv on m.id = mv.minion_id" +
                    " where mv.villain_id =?;";
    private  static final String GET_VILLAIN_NAME =
            " select v.name from villains as v" +
            " where v.id =?";
    private static final String PRINT_VILLAIN_FORMAT = "Villain: %s\n";
    private static final String PRINT_MINION_FORMAT = "%d. %s %d\n";
    public static void main(String[] args) throws SQLException {
       final Connection sqlConnection = Utils.getSqlConnection();

        final int villainId = new Scanner(System.in).nextInt();

        final PreparedStatement statementForVillains = sqlConnection.prepareStatement(GET_VILLAIN_NAME);
        statementForVillains.setInt(1,villainId);

        final  ResultSet villainResulSet = statementForVillains.executeQuery();
       if (!villainResulSet.next()){
           System.out.printf("No villain with ID 10 exists in the database.");
           return;
       };

        String villainName = villainResulSet.getString(COLUMN_LABEL_NAME);

        System.out.printf(PRINT_VILLAIN_FORMAT,villainName);

        final PreparedStatement statementForMinions = sqlConnection.prepareStatement(GET_MINIONS_NAME_AGE_BY_VILLAIN_ID);
        statementForMinions.setInt(1,villainId);

        final ResultSet minionResultSet = statementForMinions.executeQuery();

        for (int i = 1; minionResultSet.next() ; i++) {
         final String minionName = minionResultSet.getString(COLUMN_LABEL_NAME);
         final int minionAge = minionResultSet.getInt(COLUMN_LABEL_AGE);

            System.out.printf(PRINT_MINION_FORMAT,i,minionName,minionAge);
        }
        sqlConnection.close();
    }
}
