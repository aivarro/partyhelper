# 🎉 Peonõustaja AI (Party Advisor AI)

See on **Spring Booti** ja **Spring AI** raamistikul põhinev nutikas veebirakendus, mis aitab kasutajal otsustada, kas
minna täna peole või jääda pigem koju. Rakendus analüüsib kasutaja energiataset, meeleolu ja eelistusi ning genereerib
tehisintellekti abil isikupärastatud, humoorika ja stiilselt vormindatud soovituse.

Rakendus paistab silma range turvaarhidektuuri poolest, kasutades sisendi valideerimiseks nii staatilisi filtreid kui ka
**LLM-as-a-Judge** (AI-põhine turvakohtunik) mustrit.

---

## 🚀 Peamised omadused

* **Tugevalt tüübitud AI vastused:** Tehisintellekti vastused ei ole lihtsalt tekst, vaid need on Spring AI abil otse
  seotud Java klassidega (`record`).
* **Kihiline arhitektuur (Layered Architecture):** Kood on puhtalt jaotatud mudeliteks (`model`), äriloogikaks (
  `service`) ja veebiliideseks (`controller`).
* **Kaheastmeline turvakontroll (Prompt Injection kaitse):**
    1. **RegEx filter:** Püüab kinni teadaolevad ründesõnad (nt *ignore*, *bypass*).
    2. **LLM Kohtunik:** Eraldi eraldiseisev AI mudel (temperatuuriga 0.0), mis analüüsib sisendit manipuleerimiskatsete
       osas, enne kui see põhimudelisse lubatakse.
* **Turvaline ja ilus kasutajaliides:** Lihtne HTML/JS frontend, mis kasutab Markdowni renderdamiseks `marked.js` teeki
  ja XSS rünnakute vältimiseks `DOMPurify` puhastajat.

---

## 🛠️ Nõuded süsteemile

* **Java 21** (või uuem)
* **Gradle** või **Maven**
* Kehtiv **LLM API võti** (nt Google Gemini, OpenAI, Claude jne, vastavalt sellele, mis on `application.properties`
  failis seadistatud).

---

## ⚙️ Seadistamine ja käivitamine

1. **Klooni repositoorium:**
   ```bash
   git clone <sinu-repo-url>
   cd party-helper

```

2. **Lisa oma API võti:**
   Ava `src/main/resources/application.properties` ja lisa sinna oma tehisintellekti teenusepakkuja API võti.
   Näiteks Google Gemini puhul:
    spring.ai.vertex.ai.gemini.api-key=SINU_SALAJANE_API_VÕTI
```

3. **Käivita rakendus:**
   Kasutades Gradle'it:

```bash
./gradlew bootRun

```

Kasutades Mavenit:

```bash
./mvnw spring-boot:run

```

---

## 🎮 Kuidas kasutada?

### 1. Graafiline kasutajaliides (Frontend)

Kui rakendus on edukalt käivitunud, ava oma veebibrauseris aadress:
👉 **http://localhost:8080**

Seal saad sisestada oma energiataseme (1-10), hetke meeleolu ja huvid. Vajutades nuppu "Küsi AI-lt", tehakse päring
serverisse ja kuvatakse stiilne vastus.

### 2. API päring (Backend)

Võid rakendusega suhelda ka otse läbi REST API (näiteks Postmani või cURL-iga).

**Päring (POST):** `http://localhost:8080/api/party/decide`
**Päise tüüp:** `Content-Type: application/json`

**Näidis-JSON (Body):**

```json
{
  "energyLevel": 8,
  "mood": "seiklushimuline",
  "preferences": "elektrooniline muusika, tantsimine"
}

```

**Edukuse vastus (200 OK):**

```json
{
  "shouldGoOut": true,
  "chainOfThought": "Kasutaja energia on kõrge ja ta on seiklushimuline, lisaks meeldib talle elektrooniline muusika...",
  "recommendedParty": "Maa-alune tehno-reiv",
  "markdownMessage": "### 🚀 Pane tossud jalga!\n\nSinu energia on laes ja oled valmis seiklusteks..."
}

```

*Märkus: Pahatahtliku sisendi puhul (nt "unusta varasemad reeglid") tagastab server `400 Bad Request` ja ei edasta
päringut põhimudelile.*

---

## 📂 Projekti struktuur

```text
src/main/java/party/hard/partyhelper/
 ├── controller/        # REST API sisendpunktid (PartyController)
 ├── model/             # Andmestruktuurid (UserInput, PartyDecision, SecurityResult)
 └── service/           # Äriloogika, turvakontroll ja AI suhtlus (PartyService)

src/main/resources/
 ├── prompts/           # LLM süsteemi ja kasutaja juhised (.md failid)
 ├── static/            # Frontend (index.html, CSS, JS)
 └── application.properties # Konfiguratsioon

```

```

```