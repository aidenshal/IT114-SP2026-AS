# IT114-SP2026-AS

Simple Java socket chatroom project for IT114 using client/server networking.

---

## Features

- Multi-user global chat
- Direct messages (`/dm`)
- Live connected user list
- Unique usernames
- Persistent chat history (`chat_log.txt`)
- Rock Paper Scissors game system
- RPS challenge, accept, decline, and moves

---

## Files

| File | Purpose |
|---|---|
| `ChatServer.java` | Runs the server |
| `ChatClient.java` | Runs the client |
| `ClientHandler.java` | Handles connected users |
| `chat_log.txt` | Stores saved chat messages |

---

## Commands

### Direct Messages

```text
/dm username message
```

Example:

```text
/dm Rick hello
```

---

### Chat History

```text
/history
```

Displays saved chat history from the server.

---

### Rock Paper Scissors

Challenge another user:

```text
/rps challenge username
```

Accept a challenge:

```text
/rps accept username
```

Decline a challenge:

```text
/rps decline username
```

Submit a move:

```text
/rps move rock
/rps move paper
/rps move scissors
```

---

## How To Run

### Compile

```bash
javac ChatServer.java ChatClient.java ClientHandler.java
```

---

### Start Server

```bash
java ChatServer
```

---

### Start Client

```bash
java ChatClient
```


## Author

Aiden Shalaby

```
