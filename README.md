## DocsRabbit - Rendering templates to whatever you want

### Getting started

To run an instance with default configuration and without database support just run:

````
docker run -p 50005:50005 -p 50006:50006 \
    registry.gitlab.com/encircle360-oss/docsrabbit/docsrabbit:latest
````

For database support with mongodb please use this command

````
docker run -p 50005:50005 -p 50006:50006 \
    --env SPRING_PROFILES_ACTIVE=mongo \
    --env MONGO_URI=mongodb://localhost:27017/docsrabbit \
    --env MONGO_DATABASE=docsrabbit \
    registry.gitlab.com/encircle360-oss/docsrabbit/docsrabbit:latest
````

### Features of docsrabbit

* Using templates from filesystem
* Using templates out of mongodb (activate mongo profile)
* Internationalization 
* Inline rendering (you can post the template directly to render)
* Multiple rendering types: PDF, PLAIN, HTML
* Base64 encoded transfer of data 

For all available endpoints visit

`http://localhost:50005/swagger-ui/index.html?url=http://localhost:50005/v3/api-docs#/`

### Example REST calls for rendering a template
Let's render the default template from scratch 

```
curl -X POST "http://localhost:50005/render" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"format\":\"HTML\",\"templateId\":\"default\",\"model\":{\"default\":\"ich bin standart\"}}"
```

If you use a template id, where you don't have any templates in file system or database a 404 error will be returned

### Example Dockerfile

Since docsrabbit will lookup for templates in `/resources/templates/` and i18n files in `/resources/i18n/` you can use the following Dockerfile as example to create your own docker image with your own templates and i18n.
You can find examples how [templates](src/main/resources/templates) or [i18n files](src/main/resources/i18n) look like [here](src/main/resources).
```
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
