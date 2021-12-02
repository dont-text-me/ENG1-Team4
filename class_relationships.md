```plantuml
class Ship{
    -int x
    -int y
    -int health
    -int velocity
    -String college
    +Image sprite
}
class StationaryEnemy{
    -int x
    -int y
    +Image sprite
}
class Collectible{
    -int value
    -int x
    -int y
    +Image sprite
}
class Player{
    +Cannonball cannonball
}
class EnemyShip{}
class College{}
class Chest{}
class Coin{}
class Cannonball{
    -int x
    -int y
    -int velocity
    -int damage
    +Image sprite

}

Ship <|-- Player
Ship <|-- EnemyShip

StationaryEnemy <|-- College

Collectible <|-- Coin
Collectible <|-- Chest
```