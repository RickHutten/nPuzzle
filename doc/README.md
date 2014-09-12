nPuzzle
=======

In dit project wordt een android app gemaakt over het spel [15 puzzle]. 

Functies
---------
- Het is mogelijk om het niveau aan te passen in drie verschillende moeilijkheidsgraden:
    - Easy: Een grid van 3x3.
    - Normal: Een grid van 4x4, de standaard 15 puzzle.
    - Hard: Een grid van 5x5.
- Het is mogelijk om een afbeelding te selecteren om mee te spelen. Er zijn vier voorgekozen afbeeldingen om mee te spelen.
- Het is mogelijk om ipv een afbeelding genummerde vakjes te gebruiken.
- Na het selecteren van een afbeelding en moeilijkheid moet de gebruiker op een knopje drukken om naar de activity te gaan waar het spel gespeeld wordt.
- De foto moet in stukken worden geknipt in overeenstemming met de moeilijkheidsgraad. De tegel rechts onder moet weg zijn zodat er een andere tegel naartoe kan worden geschoven.
- In de "Game Activity" wordt de afbeelding drie seconden geheel getoond. Hierna wordt de foto gehusseld en kan het spel beginnen.
- Door te klikken op een tegel naast het lege vakje schuift de tegel naar de lege plek. Een tik op een tegel die niet tegen het lege vakje aanligt moet geen effect hebben.
- Tijdens het spel kan er op de menu knop geklikt worden en verschijnt er een menu waar de gebruiker het volgende mee kan:
    - Reset: de tegels worden weer geshuffled.
    - Verander de moeilijkheidsgraad: Als dit wordt gedaan reset het spel zich met de gekozen moeilijkheidsgraad.
    - Stoppen: de gebruiker gaat terug naar het vorige scherm en kan een nieuwe afbeelding kiezen.
- Het menu mag alleen verschijnen in de "Game Activity".
- De standaard moeilijkheidsgraad is normaal. Echter wanneer een gebruiker een spel op een andere moeilijkheidsgraad zet moet dit worden onthouden. Ook wanneer de app wordt gesloten.
- De spelstatus moet worden onthouden wanneer de app wordt afgesloten.
- Als de gebruiker het spel heeft gewonnen moet er een nieuwe activity worden gestart met daarop een felicitatie en de complete afbeelding, het totaal aantal moves, totale tijd en een knop om een nieuw spel te starten (terug naar het hoofdscherm). 

Code
----
Het programma wordt geschreven in xml en java. Xml wordt gebruikt voor statische elementen en java wordt gebruikt voor de dynamische content. 

De app wordt geschreven voor android met minimaal API level 11 (Android 3.0 Honeycomb), dit in tegenstelling tot de instructies van twee jaar geleden. API level 11 wordt door meer dan [87.9%] van de gebruikers ondersteund en geeft toegang tot de ActionBar APIs.

####De volgende libraries/APIs worden gebruikt:
- Er wordt veel gebruik gemaakt van de package android.widget voor Buttons, ImageViews etc.
- android.View voor het aanspreken van widgets, OnClickListeners plaatsen etc.
- Voor debugging wordt gebruik gemaakt van android.util.Log
- Voor het weergeven en knippen van afbeeldingen wordt gebruik gemaakt van android.graphics
- android.content.Intent voor het overschakelen naar een nieuwe activity
- De spelstatus wordt onthouden via de SharedPreferences APIs
- android.app.ActionBar voor het aanpassen van de ActionBar

[15 puzzle]:http://en.wikipedia.org/wiki/15_puzzle
[87.9%]:http://developer.android.com/about/dashboards/index.html