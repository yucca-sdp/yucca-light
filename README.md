# Project Title
**Yucca Smart Data Platform** è una piattaforma cloud aperta e precompetitiva della Regione Piemonte, realizzata dal CSI Piemonte con tecnologie open source.
# Getting Started
Il prodotto **Yucca Light** si occupa di integrare facilmente oggetti intelligenti con la piattaforma [Yucca](https://www.smartdatanet.it) anche in presenza di discontinuità di rete.

![Architettura](src/site/resources/images/gwiot_arch.png)

## Funzionalità
* Gateway per mediare smart-object e piattaforma Yucca
* Console Web per gestire e visualizzare lo stato del gateway
* Autoconfigurazione dalla piattaforma Yucca
* Avvio con Apache Tomcat incorporato o esterno
* Utilizzo di ActiveMQ incorporato o esterno

## Caso d'uso principale
Il caso d'uso principale di Yucca-Light è implementare un pattern di consegna garantito tra smart-object e la piattaforma Yucca remota mediante uno store locale.
Ad esempio:
1. Gli smart-object inviano eventi a un'istanza di Yucca-Light in esecuzione in una rete locale.
2. Yucca-Light prova a inviare immediatamente gli eventi a Yucca (tramite Realitme API)
3. Se Yucca non è disponibile, Yucca-Light persiste gli eventi in un broker ActiveMQ locale.
4. Tramite un processo schedulato, Yucca-Light prova a inviare gli eventi non consegnati a Yucca (tramite A2A API)

![Caso d'uso principale](src/site/resources/images/gwiot_main_usecase.png)

# Prerequisites
I prerequisiti per l'installazione del prodotto sono i seguenti:
## Software
- [OpenJDK 7](https://openjdk.java.net/install/) o equivalenti
- [Apache Maven 3](https://maven.apache.org/download.cgi)

## Configuration
Tutte le configurazioni sono impostate nel file esterno `application.properties`
Se si esegue l'applicazione in modalità stand-alone, è posizionare il file `application.properties` in una sotto-directory, denominata `config`, allo stesso livello del pacchetto di installazione.

Se si installa l'applicazione in Apache Tomcat 8 si può scegliere una delle seguenti opzioni:

* Aggiungere in `$CATALINA_BASE/conf/Context.xml` la riga:
```xml
<Parameter name="ext.application.properties" value="file:\your_custom_path\your_properties_file"/>
```
* Creare un file in `$CATALINA_BASE/conf/[enginename]/[hostname]/yucca-light.xml` con all'interno:
```xml
<Context>
<Parameter name="ext.application.properties" value="file:\your_custom_path\your_properties_file"/>
</Context>
```

La tabella seguente contiene l'elenco delle variabili della configurazione applicativa, la loro posizione all'interno del progetto e una breve descrizione o un valore di esempio.

| Percorso | Variabile | Descrizione | Obbligatoria | Valore di default |
| ---: | --- | --- |  --- |  --- | 
| application.properties | security.user.name | Username per l'accesso a Yucca-Light | sì | admin |
| application.properties | security.user.password | Password per l'accesso a Yucca-Light | sì | admin |
| application.properties | yucca.realtime.httpEndpoint | Endpoint HTTP Yucca per realtime | sì |   
| application.properties | yucca.a2a.httpEndpoint |  Endpoint HTTP Yucca per A2A | yes  |
| application.properties | yucca.metadata.httpEndpoint | Endpoint HTTP Yucca per recuperare i metadati dello stream | sì |  
| application.properties | yucca.tenant.code | Codice del tenant (es. sandbox) | yes|
| application.properties | yucca.tenant.username | Tenant tecnical username | sì |
| application.properties | yucca.tenant.password | Tenant tecnical password | sì | 
| application.properties | yucca.realtime.httpTimeout | Timeout per l'endpoint HTTP Yucca per realtime (ms) | no | 10000 |
| application.properties | yucca.a2a.httpTimeout | Timeout per l'endpoint HTTP Yucca per A2A (ms) | no | 10000 |
| application.properties | yucca.a2a.pollerMaxmessage | Il numero massimo di messaggi di fallimento da ricevere per ogni poll (reinvio di messaggi falliti) | no | 3 |
| application.properties | yucca.a2a.pollerFixeddelay | Il delay (ms) del poller (reinvio dei messaggi falliti) | no | 10000 |
| application.properties | yucca.metadata.refreshCron | Espressione Cron per l'aggiornamento dei metadati dello stream| no | 5 * * * * * |
| application.properties | yucca.metadata.refreshOnStartup | Aggiornamento dei metadati dello stream all'avvio | no | true |
| application.properties | yucca.metadata.timeToLive | Per quanto tempo vengono conservati i messaggi in stato finale (ms) | no | 864000000 |
| application.properties | spring.activemq.broker-url | URL dell'ActiveMQ esterno (o interno di default) | no | vm:(broker:(mqtt://localhost:1883)?persistent=false)?marshal=false |
| application.properties | spring.activemq.user | Username dell'ActiveMQ esterno (o interno di default) | no | admin |
| application.properties | spring.activemq.password | Password dell'ActiveMQ esterno (o interno di default) | no | secret |

All'avvio, Yucca-Light scarica automaticamente dalla piattaforma Yucca tutte le configurazioni dello stream relative al tenant configurato.

Solo i flussi persistenti sono scaricati da Yucca: i flussi non associati a data-set non sono gestiti da Yucca-Light.

# Installing
## Istruzioni per la compilazione
- Da riga di comando eseguire `mvn -Dmaven.test.skip=true -P dev clean package`
- La compilazione genera le seguenti unità di installazione:
    - `target/yucca-light.war`

## Istruzioni per l'avvio
- Verificare che la variabile di ambiente `JAVA_HOME` sia opportunamente configurata
- Verificare la confiugrazione del file `application.properties`
- Eseguire il comando `java -jar yucca-light.war`
- Da browser collegarsi all'indirizzo http://localhost:8080/yucca-light/console ed effettuare il login con le credenziali configurate precedentemente.

Se viene stampato l'errore `org.springframework.context.ApplicationContextException` controllare di aver indicato nel file `application.properties` tutte le proprietà obbligatorie.

### Installazione su Apache Tomcat 8
Il file war generato è pronto per essere installato su Apache Tomcat 8.
Per ridurre la dimensione del pacchetto è possibile rimuovere la cartella `lib-provided` presente in `yucca-light.war->WEB-INF`

## Istruzioni per l'utilizzo

### Console
Yucca-Light offre diverse console web.

**La console web principale è disponibile all'indirizzo** [http://localhost:8080/yucca-light/console](http://localhost:8080/yucca-light/console) 

Da questa cosole si può:
* visualizzare i flussi configurati su Yucca
* visualizzare gli eventi e il loro stato
* visualizzare i dettagli degli eventi con l'elenco dei tentativi di invio
* inviare nuovamente a Yucca un singolo evento utilizzando l'API Realtime o l'API A2A
  
**La console web H2 è disponibile all'indirizzo** [http://localhost:8080/yucca-light/h2](http://localhost:8080/yucca-light/h2)

Questa console standard H2 si utilizza con `org.h2.server.web.WebServlet`.

H2 viene utilizzato soltanto per salvare le configurazioni dello stream e i tentativi di invio, gli eventi vengono salvati nell'ActiveMQ locale.

**La console web delle metriche è disponbile all'indirizzo** [http://localhost:8080/yucca-light/metrics](http://localhost:8080/yucca-light/metrics)

Questa è la console delle metriche fornita da [Spring Boot Actuator](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html).

### Invio di messaggi in HTTP o MQTT
Yucca-Light espone lo stesso end-point realtime e la stessa validazione logica della piattaforma Yucca.
L'endpoint Yucca A2A (per i dati storici) non è implementato.

Si può inviare un evento tramite HTTP all'indirizzo `localhost:8080/yucca-light/api/input/smartlab` o tramite una connessione MQTT all'indirizzo `default 127.0.0.1:1883` e pubblicare l'evento in `input/<tenant>/topic`. 

Come Yucca, Yucca-Light invia i messaggi di errore sul topic MQTT `output/<tenant>/errors` o come output HTTP (in caso di chiamata HTTP) con lo stesso formato (per altre informazioni consulta la [documentazione on-line](http://developer.smartdatanet.it))

# Screenshot
**Controllo della versione Java**

![Controllo della versione Java](src/site/resources/images/gwiot_build_java_version_check.png)

**Avvio della compilazione**

![Avvio della compilazione](src/site/resources/images/gwiot_build_start_maven.png)

**Fine della compilazione**

![Fine della compilazione](src/site/resources/images/gwiot_build_end_maven.png)

**Avvio di Yucca-Light**

![Avvio di Yucca-Light](src/site/resources/images/gwiot_run_standalone_start.png)

**Yucca-Light avviata**

![Yucca-Light avviata](src/site/resources/images/gwiot_run_standalone_end.png)

**Console web principale**

![Console web principale](src/site/resources/images/gwiot_webconsole_start.png)

# Versioning
Per la gestione del codice sorgente viene utilizzata la metodologia [Semantic Versioning](https://semver.org/).
# Authors
Gli autori della piattaforma Yucca sono:
- [Alessandro Franceschetti](mailto:alessandro.franceschetti@csi.it)
- [Claudio Parodi](mailto:claudio.parodi@csi.it)
# Copyrights
(C) Copyright 2019 Regione Piemonte
# License
Questo software è distribuito con licenza [EUPL-1.2-or-later](https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12)

Consulta il file [LICENSE.txt](LICENSE.txt) per i dettagli sulla licenza.

Per le componenti che utilizzano librerie la cui licenza prevede un'integrazione, sono state inserite le informazioni necessarie nel file [THIRD_PARTY_NOTE].
