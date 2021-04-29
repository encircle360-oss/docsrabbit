curl --location --request POST 'http://localhost:50005/render' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "templateId": "html/default",
    "format": "PDF",
    "locale": "en",
    "model": {
        "title": "Fresh CFA schemas Plastic interface",
        "date": "2021-03-10T11:05:09.816",
        "address": {
            "firstName": "Jane",
            "lastName": "Champlin",
            "street": "381 Blick Parks",
            "houseNumber": "194",
            "zip": "595932",
            "city": "Coltmouth"
        },
        "subject": "Customizable compressing orchestration Investor",
        "text": "Et itaque ratione. In suscipit error dolorem occaecati eos .",
        "sender": {
            "firstName": "Cale",
            "lastName": "Bartoletti"
        }
    }
}' | jq -r .base64 | base64 -D >~/Desktop/test-en.pdf

curl --location --request POST 'http://localhost:50005/render' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "templateId": "html/default",
    "format": "PDF",
    "locale": "de",
    "model": {
        "title": "Fresh CFA schemas Plastic interface",
        "date": "2021-03-10T11:05:09.816",
        "address": {
            "firstName": "Jane",
            "lastName": "Champlin",
            "street": "381 Blick Parks",
            "houseNumber": "194",
            "zip": "595932",
            "city": "Coltmouth"
        },
        "subject": "Customizable compressing orchestration Investor",
        "text": "Et itaque ratione. In suscipit error dolorem occaecati eos fugiat omnis nihil. Non perspiciatis adipisci et quaerat dolorem autem sit atque neque. Qui aliquid sint libero. Suscipit accusantium sit qui quia cum quis sit culpa dolorum. Ut et omnis ea doloremque eveniet.",
        "sender": {
            "firstName": "Cale",
            "lastName": "Bartoletti"
        }
    }
}' | jq -r .base64 | base64 -D >~/Desktop/test-de.pdf

open ~/Desktop/test-en.pdf
open ~/Desktop/test-de.pdf
