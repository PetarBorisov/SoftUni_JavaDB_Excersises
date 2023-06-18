import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class IncreaseMinionsAge {
    private static final String UPDATE_MINIONS_NAME_AND_AGE_BY_ID =
            "UPDATE minions SET age = age + 1, name = lower(name) WHERE id = ?";
    private static final String GET_INFO_FOR_MINIONS = "SELECT name, age FROM minions";
    private static final String PRINT_ALL_MINIONS = "%s %d%n";

    public static void main(String[] args) throws SQLException {
        Connection connection = Utils.getSqlConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MINIONS_NAME_AND_AGE_BY_ID);
        Scanner scanner = new Scanner(System.in);
        List<Integer> minionsIds = Arrays.stream(scanner.nextLine().split("\\s+"))
                .map(Integer::parseInt).collect(Collectors.toList());

        for (int index = 0; index < minionsIds.size(); index++) {
            preparedStatement.setInt(1, minionsIds.get(index));
            preparedStatement.executeUpdate();
        }

        printAllMinions(connection);

        connection.close();
    }
    private static void printAllMinions(Connection connection) throws SQLException {
        PreparedStatement allMinionsInfo = connection.prepareStatement(GET_INFO_FOR_MINIONS);
        ResultSet resultSet = allMinionsInfo.executeQuery();

        while (resultSet.next()) {
            System.out.printf(PRINT_ALL_MINIONS, resultSet.getString(Constants.COLUMN_LABEL_NAME), resultSet.getInt("age"));
        }
    }
}