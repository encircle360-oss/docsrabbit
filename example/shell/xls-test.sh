curl --location --request POST 'http://localhost:50005/render' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "templateId": "xls/default",
    "format": "XLS",
    "locale": "de",
    "model": {
        "employees": [
            {
                "name": "Yvette Stoltenberg",
                "age": 22,
                "payment": 780.39,
                "bonus": 1.4,
                "birthDate": "03.10.1976"
            },
            {
                "name": "Callie Wisoky",
                "age": 36,
                "payment": 896.78,
                "bonus": 4.2,
                "birthDate": "03.10.1976"
            },
            {
                "name": "Jefferey Huels",
                "age": 38,
                "payment": 835.05,
                "bonus": 9.2,
                "birthDate": "03.10.1976"
            },
            {
                "name": "Foster Stroman",
                "age": 31,
                "payment": 297.2,
                "bonus": 0.2,
                "birthDate": "03.10.1976"
            },
            {
                "name": "Ora Cummerata",
                "age": 45,
                "payment": 569.72,
                "bonus": 22.2,
                "birthDate": "03.10.1976"
            }
        ]
    }
}' | jq -r .base64 | base64 -D > test.xlsx

open test.xlsx
