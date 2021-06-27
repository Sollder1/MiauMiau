import ChatMessage from "./ChatMessage";

export interface Notification {

    event: string;
    playerId: string;

}

export interface LobbyNotification extends Notification {

    playerName: string;
    lobbyId: string;
    lobbyName: string;

}

export interface GameUpdateNotification extends Notification {

    lobbyStarted: boolean;
    currentPlayerId: string;
    //See CardCodes later...

    //The last card on the stack
    lastCardOnStack: string;
    //The cards you had before the round
    myCards: string[];
    //The cards you get with this update.
    //Will be filled whenever a player draws a card.
    myNewCards: string[];

}

export interface IssueNotification extends Notification {
    text: string;
}

export interface ChatMessageNotification extends Notification, ChatMessage {

}