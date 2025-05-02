curl.exe -X DELETE http://localhost:8083/connectors/budget-postgres-connector

curl.exe -X POST -H "Content-Type: application/json" --data '@configuration.json' http://localhost:8083/connectors


