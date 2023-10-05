Reproduce this issue:

https://github.com/microsoft/mssql-jdbc/issues/2222#issuecomment-1747290705

The server can be setup by the follwowing:

````
podman run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=Password1" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2022-latest
```