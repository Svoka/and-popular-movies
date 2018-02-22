To use this app, create 

**apistore.properties** 

outside project directory.

Content of the file must be looking like this

`apiKey=xxxx_here_comes_your_key_xxxxx`

OR

Replace line 20 in /app/build.gradle

`` buildConfigField('String', 'API_KEY', '"' + apiProperties['apiKey'] + '"')``

with this

` buildConfigField('String', 'API_KEY', '"#HERE_IS_YOUR_API_KEY#"')`