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


