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
                "age": 242,
                "payment": 780.39,
                "bonus": 1.4,
                "birthDate": "Tue Mar 16 2021 02:45:25 GMT+0100 (Central European Standard Time)"
            },
            {
                "name": "Callie Wisoky",
                "age": 376,
                "payment": 896.78,
                "bonus": 4.2,
                "birthDate": "Tue Mar 16 2021 08:15:07 GMT+0100 (Central European Standard Time)"
            },
            {
                "name": "Jefferey Huels",
                "age": 398,
                "payment": 835.05,
                "bonus": 9.2,
                "birthDate": "Mon Mar 15 2021 16:29:38 GMT+0100 (Central European Standard Time)"
            },
            {
                "name": "Foster Stroman",
                "age": 391,
                "payment": 297.2,
                "bonus": 0.2,
                "birthDate": "Tue Mar 16 2021 08:01:05 GMT+0100 (Central European Standard Time)"
            },
            {
                "name": "Ora Cummerata",
                "age": 405,
                "payment": 569.72,
                "bonus": 22.2,
                "birthDate": "Mon Mar 15 2021 15:18:12 GMT+0100 (Central European Standard Time)"
            }
        ]
    }
}' | jq -r .base64 | base64 -D >~/Desktop/test.xlsx

open ~/Desktop/test.xlsx
