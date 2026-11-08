# 🗺️ Roadmap de Desenvolupament: RogueSweeper

Aquest document serveix com a guia pas a pas per al desenvolupament de **RogueSweeper**. Detalla les fases del projecte, les fites clau i la distribució de tasques entre el treball conjunt i el treball individual de cada membre.

---

## 📌 Taula de Fases i Progrés

| Fase | Descripció | Estat |
| :--- | :--- | :---: |
| **Fase 1** | Proposta, conceptualització i aprovació del projecte | ✅ **Completada** |
| **Fase 2** | Arquitectura base, MVP jugable i Persistència en Fitxers (RA1) | ⏳ **Pendent d'iniciar** |
| **Fase 3** | Persistència en Base de Dades Relacional (RA2 - MariaDB) | 📋 Pendent |
| **Fase 4** | Ampliacions opcionals, so i poliment visual | 📋 Pendent |
| **Fase 5** | Proves finals, tancament de documentació i lliurament | 📋 Pendent |

---

## 🎯 Fase 1: Proposta i Disseny Inicial (Feta ✅)

- [x] **Junts:** Pluja d'idees del joc i fusió de conceptes (buscamines clàssic + mecàniques *roguelite*).
- [x] **Junts:** Redacció del document de proposta amb regles, exemple de partida, MVP, dades i requeriments RA1/RA2.
- [x] **Junts:** Disseny de l'arquitectura en 3 capes desacoblades (`presentation`, `domain`, `data`).
- [x] **Junts:** Creació del repositori de GitHub i configuració del [README.md](file:///c:/Users/David/Documents/GitHub/RogueSweeper/README.md).

---

## 🚀 Fase 2: Fonaments, Primera Versió Jugable (MVP) i Fitxers (RA1)

Aquesta és la fase central on el joc pren vida per primera vegada i s'implementa la persistència local en fitxers.

### 🤝 Tasques Conjuntes (David & Iker)
- **Definició dels contractes de dades (`domain/model`):**
  - Acordar les `data classes` de Kotlin que compartireu: `Cell`, `Board`, `PlayerProfile`, `Upgrade`, `GameResult`.
  - Definir la signatura de la interfície `IProfileRepository` (funcions com `saveProfile()`, `loadProfile()`, `resetProfile()`, `exportToCsv()`).
- **Definició dels estats de pantalla:**
  - Acordar quines dades necessita la UI per pintar-se (nombre d'escuts, saldo, graella de cel·les) i quines accions envia l'usuari a la lògica (descobrir cel·la, posar bandera).
- **Proves d'integració inicials:**
  - Connectar el motor d'en David amb la primera pantalla de joc de l'Iker.
  - Validar que prémer una casella a la pantalla executa la revelació del motor correctament.

---

### ⚙️ Tasques de David (Lògica de Joc & Accés a Dades RA1)
*Pots desenvolupar i provar tot això amb tests unitaris JUnit o amb un `main()` de consola sense necessitat de pantalles.*

- **Motor algorítmic del buscamines (`domain/engine/MinesweeperEngine.kt`):**
  - Generació del tauler (8x8) i posicionament aleatori de 10 mines (assegurar que el primer clic mai sigui mina).
  - Càlcul de mines veïnes per a cada casella (números de l'1 al 8).
  - Algorisme recursiu o iteratiu d'obertura en cascada (*flood fill*) quan es revela una casella buida (amb 0 veïns).
- **Regles roguelite i puntuacions (`domain/service/`):**
  - Control de condició de victòria (totes les caselles sense mina revelades) i derrota (detonació sense escuts).
  - Sistema d'escuts: si el jugador té $\ge 1$ escut, absorbir la detonació i continuar la partida.
  - Càlcul de punts (`ScoreManager.kt`): sumar punts per mines detectades amb bandera i càlcul del bonus final de victòria.
  - Lògica de compra de millores (`UpgradeService.kt`): comprovar saldo i augmentar nivell.
- **Persistència en Fitxers JSON (`data/file/JsonProfileRepository.kt`):**
  - Afegir la dependència `kotlinx.serialization` al projecte.
  - Implementar desar i carregar ranures de perfil (`saves/slot_1.json`, `saves/slot_2.json`).
  - Gestió d'errors (si el fitxer no existeix, crear un perfil nou per defecte).
- **Conversió JSON ↔ CSV (`data/file/FormatConverter.kt`):**
  - Exportar el resum de perfil a un fitxer pla CSV (`perfil_resum.csv`).
  - Importar dades des del CSV per actualitzar o reconstruir el perfil JSON.
- **Accés aleatori binari (`data/file/BinaryRecordsManager.kt`):**
  - Implementar l'escriptura i lectura de registres amb `RandomAccessFile` per guardar històrics o logs de partides en format binari.

---

### 🎨 Tasques d'Iker (Presentació i Interfície d'Usuari UI/UX)
*Pots maquetar les pantalles primer amb dades simulades (mocks) per avançar en paral·lel.*

- **Estructura de navegació de l'aplicació (`presentation/screens/`):**
  - **Menú Principal / Selecció de Slot:** Botons per triar `slot_1` o `slot_2`, veure el perfil actual, botó per jugar, botó per anar a la botiga i opció per reiniciar dades.
  - **Pantalla de Joc (`GameScreen`):** Zona superior d'informació (HUD) i zona central amb el tauler interactiu.
  - **Pantalla de Botiga (`ShopScreen`):** Llistat visual d'atributs millorables (escuts, caselles inicials, etc.) amb els seus nivells actuals, costos en punts i botó de compra.
- **Graella interactiva de caselles (`presentation/components/`):**
  - Disseny del component casella: estat tapat, revelat, bandera posada, mina detonada, colors dels números (1 blau, 2 verd, 3 vermell, etc.).
  - Distribució dinàmica de la graella de cel·les (8x8) adaptada a la mida de la pantalla mòbil.
- **Gestió d'esdeveniments d'entrada (`presentation/InputController.kt`):**
  - Detectar el tipus de gest sobre cada casella:
    - **Toc curt (*Tap*):** cridar a descobrir casella.
    - **Toc llarg (*Long Press*):** cridar a col·locar o treure bandera.
- **Panell superior (HUD):**
  - Comptadors visuals en temps real: mines restants pendents de marcar, escuts disponibles i punts acumulats a la partida.
- **Diàlegs finals de partida:**
  - Diàleg de **Victòria:** mostrar punts guanyats + bonus de victòria, i botó per anar a cobrar i sortir al menú.
  - Diàleg de **Derrota:** avisar de la detonació fatal, mostrar punts conservats i botó de tornada.

---

## 🗄️ Fase 3: Persistència Relacional amb MariaDB (RA2)

En aquesta fase s'afegeix el suport complet per a base de dades relacional sense trencar la interfície gràfica gràcies a l'arquitectura en capes.

### 🤝 Tasques Conjuntes (David & Iker)
- **Revisió del model de dades relacional:**
  - Verificar conjuntament les taules necessàries: `jugadors`, `cartera`, `millores_jugador` i `partides`.
- **Flux d'usuari amb Base de Dades:**
  - Decidir com l'usuari tria si vol jugar en mode local (Fitxers) o sincronitzat amb el servidor MariaDB.
- **Validació de la transacció de compra:**
  - Provar que si la compra falla a la botiga (per exemple, per falta de connexió o saldo), la interfície ho notifica correctament sense restar punts.

---

### ⚙️ Tasques de David (Backend Relacional & MariaDB)
- **Connexió JDBC (`data/database/MariaDbConnection.kt`):**
  - Afegir el driver JDBC de MariaDB al fitxer de configuració de dependències.
  - Gestionar la connexió amb la base de dades (IP, port, usuari, contrasenya) de manera segura.
- **Scripts DDL de creació:**
  - Escriure l'script SQL per crear les taules amb claus primàries, foranes i restriccions (`ON DELETE CASCADE`).
- **Implementació del repositori SQL (`data/database/SqlProfileRepository.kt`):**
  - Implementar les operacions CRUD mitjançant `PreparedStatement`:
    - `CREATE`: Registrar nous jugadors i guardar resums de partides.
    - `READ`: Carregar perfil, consultar millores del jugador i extreure el rànquing (*Leaderboard*).
    - `UPDATE`: Actualitzar saldo de cartera i nivells de millora.
    - `DELETE`: Eliminar perfil o reiniciar dades.
- **Procediment emmagatzemat (`sp_liquidar_partida`):**
  - Programar a MariaDB el procediment que rep els paràmetres de la partida, calcula el bonus final, insereix la fila a `partides` i abona de forma atòmica els punts a `cartera`.
- **Transacció de compra a la botiga (`TransactionHelper.kt`):**
  - Implementar una transacció atòmica amb `connection.autoCommit = false`:
    1. Comprovar saldo a `cartera`.
    2. Restar cost dels punts.
    3. Incrementar nivell a `millores_jugador`.
    4. `commit()` o `rollback()` en cas d'error.

---

### 🎨 Tasques d'Iker (Interfície d'Estadístiques i Rànquings)
- **Pantalla o pestanya de Rànquing / Historial:**
  - Afegir una vista per mostrar els millors jugadors (obtinguts de la consulta SQL de rànquing).
  - Mostrar la llista de partides recents del perfil (punts, victòria/derrota, data).
- **Feedback visual de connexió:**
  - Indicador d'estat de connexió a MariaDB (connectat / mode offline de fitxers).
  - Diàleg informatiu o `Snackbar` si es produeix un error de xarxa en fer una transacció.

---

## 🔮 Fase 4: Ampliacions Opcionals i Poliment (Post-MVP)

Aquesta fase aborda les millores opcionals un cop el nucli i els dos RAs estan completats i validats.

### 🤝 Tasques Conjuntes
- **Balanç de joc (*Playtesting*):**
  - Ajustar preus de la botiga, punts per mina i bonus de victòria perquè l'experiència roguelite sigui satisfactòria.

### ⚙️ Tasques de David
- **Mode Contrarellotge (Time Attack):**
  - Motor de càlcul de temps transcorregut i multiplicadors decreixents de punts per segon.
- **Lògica d'Assoliments (*Achievements*):**
  - Verificador de fites (ex: "Desarmar 50 mines", "Guanyar la primera partida sense detonar cap escut").

### 🎨 Tasques d'Iker
- **Feedback Hàptic i Efectes:**
  - Vibració del dispositiu en detonar una mina o en prémer caselles.
  - Integració d'efectes sonors senzills (clic de casella, xiulet de victòria, explosió, so de monedes a la botiga).
- **Animacions visuals:**
  - Animació suau de cascada en obrir zones buides.
  - Efecte de sacsejada de pantalla (*screen shake*) en perdre un escut.

---

## 📦 Fase 5: Proves Finals, Documentació i Lliurament

### 🤝 Tasques Conjuntes
- **Proves creuades (*Cross-Testing*):**
  - Provar el joc complet en dispositius reals o emuladors diferents.
  - Comprovar que el canvi entre ranures (`slot1` vs `slot2`) no barreja les dades.
  - Comprovar l'exportació i reimportació del CSV.
- **Revisió de l'entrega segons la rúbrica del docent:**
  - Validació dels requisits de RA1 (fitxers JSON, CSV, accés aleatori).
  - Validació dels requisits de RA2 (MariaDB, CRUD, Stored Procedure, Transaccions ACID).
- **Preparació de la memòria / presentació:**
  - Documentar com configurar el servidor de base de dades per a la correcció del professor.
  - Assegurar que el codi està net, ben comentat i amb l'arquitectura de 3 capes respectada.
