<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <style>
        .center {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
        }
    </style>
    <title>Registration Car</title>
</head>
<body style="background-color: lightseagreen">
<div class="center">

    <h1>Generate number for car:</h1>
    <h2>${nextNumber}</h2>

    <a href="/number/next" style="text-align: center; display:inline; padding: 10px; text-decoration: none; border: 1px solid black; background: white; border-radius: 5px; box-shadow: 0 0 4px rgba(0,0,0,0.4);">
        Next
    </a>
    <a href="/number/random" style="display:inline; padding: 10px; text-decoration: none; border: 1px solid black; background: white; border-radius: 5px; box-shadow: 0 0 4px rgba(0,0,0,0.4);">
        Random
    </a>
</div>
</body>
</html>
