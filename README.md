# Mcmg
MCSU refactored refactored
> Note to self, stop making new repositories

# Game structure

Games have gamestates, these can be ones such as lobby, active (during the game), or base ones which are active the whole time (useful for things such as wrappers like game players to pre initialise)

This means you will have

```
> Create Lobby Game State
-> Returns a new instance of a lobby game state
> Create Active Game State
-> Returns a new instance of a active game state
> Create Game States
-> Returns a list of new instances of a any other game state
```

All of these are called once at init, and any gamestates registered after this are regarded as temporary, and are removed after the game is finished
