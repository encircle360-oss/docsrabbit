curl --location --request POST 'http://localhost:50005/render' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "templateId": "xls/default",
    "format": "XLS",
    "locale": "de",
    "model": {
        "sheets": [
            {
            "name":"test1",
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
                        }
                        ]
            },
            {
                        "name":"test2",
                         "employees": [
                                    {
                                        "name": "Yvetadsate Stoltenberg",
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
                                    }
                                    ]
                        }
        ]
    }
}' | jq -r .base64 | base64 -D > test.xlsx

open test.xlsx
