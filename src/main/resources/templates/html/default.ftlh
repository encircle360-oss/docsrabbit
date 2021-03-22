<!DOCTYPE HTML>
<html lang="de">
<head>
    <meta charset="utf-8">
    <style type="text/css">
        * {
            font-family: Helvetica, Verdana, Arial, sans-serif;
        }

        .content {
            margin: 10px;
            padding-top: 50px;
            font-size: 1.50em;
        }

        .address {
            text-align: left;
        }

        .date {
            float: right;
        }

        .subject {
            clear: both;
            font-weight: bold;
            padding-top: 80px;
        }

        .text {

        }

        .footer {
            padding-top: 50px;
        }
    </style>
    <title>${title}</title>
</head>
<body class="content">
<div class="address">
    ${address.firstName!""} ${address.lastName!""}<br/>
    ${address.street!""} ${address.houseNumber!""} <br/>
    ${address.zip!""} ${address.city!""} <br/>
</div>

<div class="date">
    ${date?date("yyyy-MM-dd'T'HH:mm:ss.SSS")?string.long}
</div>

<div class="subject">
    <@spring.message "default.letter.subject"/>: ${subject!""}
</div>

<div class="text">
    <p>
        ${text!""}
    </p>
</div>

<div class="footer">
    <@spring.message "default.letter.footer"/>
    <br/><br/>
    ${sender.firstName!""} ${sender.lastName!""}<br/>
</div>
</body>
</html>
