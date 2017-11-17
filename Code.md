```XAML
class SQLiteHelper
{
    static string DbPath = Path.Combine(YatesHelper.GetAppDefaultPath(), "Database");

    private static SQLiteConnection CreateDatabaseConnection(string dbName = null)
    {
        if (!string.IsNullOrEmpty(DbPath) && !Directory.Exists(DbPath))
            Directory.CreateDirectory(DbPath);
        dbName = dbName == null ? "database.db" : dbName;
        var dbFilePath = Path.Combine(DbPath, dbName);
        return new SQLiteConnection("DataSource = " + dbFilePath);
    }

    static SQLiteHelper()
    {
        Open(connection);
        using (var tr = connection.BeginTransaction())
        {
            var materialInitSQL = "create table if not exists Materials();";
            ExecuteCommand(materialInitSQL);
            tr.Commit();
        }
    }

    public static void DeleteDatabase(string dbName)
    {
        var path = Path.Combine(DbPath, dbName);
        File.Delete(path);
    }

    public static List<Material> GetMaterialDatas()
    {
        var MaterialList = new List<Material>();
        Open(connection);
        using (var tr = connection.BeginTransaction())
        {
            var queryAll = "select * from Materials;";
            using (var command = connection.CreateCommand())
            {
                command.CommandText = queryAll;
                var reader = command.ExecuteReader();
                while (reader.Read())
                {

                }
            }

            tr.Commit();
        }
        return MaterialList;
    }

    public static void ExecuteNoQuery(string sql)
    {
        Open(connection);
        using (var tr = connection.BeginTransaction())
        {
            ExecuteCommand(sql);
            tr.Commit();
        }
    }

    private static void ExecuteCommand(string sql)
    {
        using (var command = connection.CreateCommand())
        {
            command.CommandText = sql;
            command.ExecuteNonQuery();
        }

    }
    private static void Open(SQLiteConnection connection)
    {
        if (connection.State != System.Data.ConnectionState.Open)
        {
            connection.Open();
        }
    }

    private static SQLiteConnection connection = CreateDatabaseConnection();
}
```
