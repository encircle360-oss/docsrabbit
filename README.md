[![pipeline status](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/badges/master/pipeline.svg)](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/commits/master)
[![latest version](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/jobs/artifacts/features/documentation/raw/badges/latestversion.svg?job=create-badges)](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/tags)
[![commits](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/jobs/artifacts/features/documentation/raw/badges/commits.svg?job=create-badges)](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/commits)
[![licence](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/jobs/artifacts/features/documentation/raw/badges/license.svg?job=create-badges)](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/blob/master/LICENSE)
[![awesomeness](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/jobs/artifacts/features/documentation/raw/badges/awesomeness.svg?job=create-badges)](https://www.encircle360.com)

## DocsRabbit - Render templates | Scan document

##### Render templates the way you want. Scan documents of many standard file types.

### Getting started

To run an instance with default configuration and without database support just run:

```shell
docker run -p 50005:50005 -p 51005:51005 \
    registry.gitlab.com/encircle360-oss/docsrabbit/docsrabbit:latest
```

For database support with mongodb please use this command

```shell
docker run -p 50005:50005 -p 51005:51005 \
    --env SPRING_PROFILES_ACTIVE=mongo \
    --env MONGO_URI=mongodb://localhost:27017/docsrabbit \
    --env MONGO_DATABASE=docsrabbit \
    registry.gitlab.com/encircle360-oss/docsrabbit/docsrabbit:latest
```

### Features of docsrabbit

Rendering: 
* Using templates from filesystem
* Using templates out of mongodb (activate mongo profile)
* Internationalization 
* Inline rendering (you can post the template directly to render)
* Multiple rendering types: PDF, PLAIN, HTML
* Base64 encoded transfer of data
  
OCR: 
* OCR scans for files like PDF, Word, Excel, etc. 

For all available endpoints visit the OpenAPI specs:

`http://localhost:50005/swagger-ui/index.html?url=http://localhost:50005/v3/api-docs#/`

### Example REST call for rendering a template
Let's render the default template from scratch 

```shell
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
}'
```

### Example REST call for a OCR scan from a file 

```shell
curl -X 'POST' \
  'http://localhost:50005/ocr' \
  -H 'accept: application/json' \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path--to-files/example.pdf;type=application/pdf'
```

### Example Dockerfile

Since docsrabbit will lookup for templates in `/resources/templates/` and i18n files in `/resources/i18n/` you can use the following Dockerfile as example to create your own docker image with your own templates and i18n.
You can find examples how [templates](src/main/resources/templates) or [i18n files](src/main/resources/i18n) look like [here](src/main/resources).
```dockerfile
FROM registry.gitlab.com/encircle360-oss/docsrabbit/docsrabbit:latest
ADD templates /resources/templates # add your template directory containing *.ftlh templates here
ADD i18n /resources/i18n # add your i18n directory containing messages.properties files here
```

If you're done with this you can build your own image using docker-cli `docker build .` or let your build pipeline do that.
E.g. we suggest to use gitlab-ci to always have your own customized docsrabbit docker image.

After you've build your own docker image with your own templates you can use the REST api to render documents.
The `templateId` field corresponds to the template filename. If you've added a template called `invoice.ftlh` you have to use
`"invoice"`
within your API request payload.

### Service Health

If you started with port exposing to localhost you can fetch `http://localhost:50006/actuator/health` to get health status of the service itself 


### Good to know
Docsrabbit internally uses the [freemarker](https://freemarker.apache.org/) template engine which has the advantages that it's easy to copy and paste email html templates.
This is really useful if you for example use email templates bought on themeforest. Since these templates can get updates you don't have to check each html dom element while importing a template update.
Mostly you only have to focus on your content model variables and you're able to just copy the html from the update.

Templates will be used for body and subject, too. So you can use i18n features also in your subject.

So you have the [full feature support](https://freemarker.apache.org/docs/ref.html) of freemarker in your templates and also activated i18n support so that you can also put your resource bundles with messages.properties into your image and switch the locale for each email you're sending.

This is open source software by [encircle360](https://encircle360.com).
Use on your own risk and for personal use. If you need support or consultancy just contact us.
