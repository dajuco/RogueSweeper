# 💣 RogueSweeper

> **Un buscamines tàctil per a Android reinventat amb mecàniques Roguelike i progressió permanent.**

![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-blueviolet?logo=kotlin&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Android_API_24+-green?logo=android&logoColor=white)
![Genre](https://img.shields.io/badge/Genre-Puzzle%20%2F%20Roguelite-orange)
![Architecture](https://img.shields.io/badge/Architecture-3--Layer_Clean_Architecture-informational)

---

## 👥 Membres de l'Equip

| Membre | Àrea Principal | Responsabilitats |
| :--- | :--- | :--- |
| **Iker Lopez** | 🎨 **Presentació i Interfície (UI/UX)** | Disseny gràfic mòbil, graella interactiva, pantalles de joc/botiga/menú, control d'esdeveniments tàctils. |
| **David Judez** | ⚙️ **Lògica de Joc i Dades (Core)** | Motor algorítmic del buscamines, càlcul de puntuacions, regles roguelite i persistència de perfils. |

---

## 🎮 Concepte i Gameplay Loop

**RogueSweeper** transforma el clàssic trencaclosques del buscamines en una experiència *roguelite*: cada decisió compta, els errors tenen segones oportunitats gràcies a millores desbloquejables, i el progrés acumulat entre partides permet afrontar reptes cada cop més exigents.

```
    ┌────────────────────────┐
    │     Inici Partida      │◄─────────────────────────────┐
    │  (Tauler, Mines, Vides)│                              │
    └───────────┬────────────┘                              │
                │                                           │
                ▼                                           │
    ┌────────────────────────┐                              │
    │  Exploració & Banderes │                              │
    │  • Descobrir casella   │                              │
    │  • Marcar mina         │                              │
    └───────────┬────────────┘                              │
                │                                           │
        ┌───────┴───────┐                                   │
   Mina │               │ Victòria                          │
(Escut?)│               │ (Tot net)                         │
        ▼               ▼                                   │
┌──────────────┐ ┌──────────────┐                           │
│   Derrota    │ │   Victòria   │                           │
│(Cobra punts) │ │(+Bonus punts)│                           │
└───────┬──────┘ └──────┬───────┘                           │
        │               │                                   │
        └───────┬───────┘                                   │
                ▼                                           │
    ┌────────────────────────┐                              │
    │   Botiga de Millores   │                              │
    │  (Inversió permanent)  ├──────────────────────────────┘
    └────────────────────────┘
```

### 🎯 Objectiu del Joc
Netejar taulers de mines deduint caselles segures. Per cada casella netejada i cada mina correctament identificada o esquivada s'obtenen punts. Aquests punts es transfereixen al **saldo permanent** del jugador en acabar la partida (tant si es guanya com si es perd), permetent comprar millores permanents a la botiga.

### 🕹️ Controls i Accions Disponibles
- **Descobrir casella:** Toc curt sobre una casella coberta per revelar el seu contingut o desencadenar una obertura en cascada d'àrees buides.
- **Col·locar / Treure bandera:** Toc mantingut per protegir la casella contra tocs accidentals i sumar punts de mina detectada.
- **Botiga de millores:** Accés entre partides per gastar el saldo acumulat i millorar atributs.
- **Gestió de perfils:** Selecció de ranura de guardat (*slot*), opció de reiniciar el progrés o consultar estadístiques.

### 🛡️ Regles, Escuts i Condicions de Partida
- **Sistema d'Escuts:** A diferència del buscamines tradicional, trepitjar una mina no sempre implica *Game Over*. Si el jugador disposa d'escuts actius, l'escut absorbeix la detonació i la partida continua.
- **Condició de Victòria:** Descobrir totes les caselles sense mina. S'afegeix un **bonus especial de victòria** a tots els punts aconseguits durant la partida.
- **Condició de Derrota:** Detonar una mina un cop esgotats tots els escuts disponibles. La partida finalitza, però **es conserven tots els punts acumulats** fins a aquell moment.
- **Progressió Permanent:** Els punts no es perden mai; es cobren a la cartera del jugador per invertir-los en la següent temptativa.

---

## 🛒 Botiga de Millores Permanents (Upgrades)

A la botiga entre partides, el jugador pot invertir els seus punts en diferents atributs per personalitzar el seu estil de joc:

| Millora | Efecte en el Joc |
| :--- | :--- |
| 🛡️ **Capacitat d'Escuts** | Permet sobreviure a més d'una detonació accidental per partida. |
| 🔍 **Revelació Inicial** | Comença cada partida amb una petita àrea segura revelada automàticament. |
| ⭐ **Valor de Mina** | Augmenta els punts obtinguts per cada mina detectada o esquivada amb èxit. |
| 🗺️ **Mida del Tauler** | Desbloqueja taulers més grans amb major densitat de mines i millors recompenses. |

---

## 📖 Exemple d'una Partida

1. **Inici:** El jugador comença amb un tauler de 8x8, 10 mines amagades i 1 escut actiu adquirit prèviament.
2. **Desenvolupament:** Marca 3 banderes en mines deduïdes amb certesa (+30 punts).
3. **Imprevist:** En prémer una casella de risc, toca una mina: **l'escut s'activa i absorbeix la detonació**, salvant la partida.
4. **Resolució:** Completa la resta de caselles segures amb èxit (+70 punts de mines restants).
5. **Recompensa:** Suma un bonus de victòria de +100 punts. En tornar al menú principal cobra un total de **200 punts**.
6. **Progrés:** Accedeix a la botiga i compra l'actualització **"Escut Nivell 2"** per a la propera partida.

---

## 🚀 Funcionalitats del Projecte

### 🌟 Versió 1.0 (MVP Imprescindible)
- [x] **Interfície tàctil mòbil:** Graella interactiva fluida adaptada a pantalles tàctils amb botons bàsics.
- [x] **Lògica del buscamines:** Col·locació aleatòria de mines, càlcul automàtic de veïns i revelació en cascada de zones buides.
- [x] **Mecàniques roguelite:** Control d'escuts/vides, comptador de punts i detecció automàtica de victòria o derrota.
- [x] **Botiga de millores:** Sistema d'adquisició de millores amb cost incremental.
- [x] **Persistència de perfils:** Desa i càrrega de punts, millores i ranures de perfil (`slot1`, `slot2`) amb opció de reinici.

### 🔮 Ampliacions Opcionals
- [ ] **Mode Contrarellotge:** Multiplicadors de punts en funció del temps de resolució.
- [ ] **Sistema d'Assoliments (*Achievements*):** Recompenses per assolir fites concretes de partides i mines.
- [ ] **Efectes audiovisuals:** Feedback hàptic (vibració) i efectes sonors per a detonacions, victòries i compres.

---

## 🏗️ Organització del Codi (Separació en 3 Capes)

L'arquitectura del projecte es divideix en tres capes desacoblades per mantenir el codi net i modular:

```
com.roguesweeper/
├── presentation/                  # CAPA 1: Presentació (UI Mòbil)
│   ├── screens/                   # Pantalles de joc, botiga i selecció de perfil
│   ├── components/                # Graella interactiva de caselles i marcadors
│   └── InputController.kt         # Gestió d'esdeveniments tàctils (toc curt / mantingut)
│
├── domain/                        # CAPA 2: Lògica del Joc
│   ├── model/                     # Data classes pures (Board, Cell, PlayerProfile, Upgrade)
│   ├── engine/                    # MinesweeperEngine.kt: algorismes de mines, buits i adjacències
│   ├── service/                   # UpgradeService.kt, ScoreManager.kt
│   └── repository/                # Interfície d'accés a dades (IProfileRepository)
│
└── data/                          # CAPA 3: Accés a Dades
    ├── file/                      # Persistència en fitxers locals (JSON, CSV, RandomAccessFile)
    └── database/                  # Persistència en base de dades (MariaDB)
```
