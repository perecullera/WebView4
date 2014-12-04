WebView4
========
primera versió de la app

és la versió més simple amb 2 activities/pàgines

**1a pàgina activity**

demana server i port i ho passa via intent a la següent activity (webviewactivity)

**2a pàgina webViewActivity**

recull server i port i obre un webview amb http://server:port

ToDo
=====
* controlar errors/excepcions
* controlar el SharedPreferences: esborrar al tancar l'app, guradar preferencies....

Install
=======


2a versió (per implementar en un fork nou)
==========
1 a opció: Des del botó settings al ActionBar controlar server i port i un listener 
q automaticament controli els canvis de settings

2a opció: q a l'entrar contoli si hi ha cap preferència guardada obri directament allí i sinó q demani server i port i després obrir
