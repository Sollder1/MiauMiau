import App from "../App";
import LocaleStorageFacade from "../common/LocaleStorageFacade";

class WebsocketHandler {

    private static websocket: WebSocket;
    private static app: App;

    public static connect(name: string, app: App) {

        WebsocketHandler.app = app;
        let id: string = WebsocketHandler.getId();

        WebsocketHandler.websocket = new WebSocket("ws://localhost:8080/skippo_server/websocket/" + name + "/" + id);

        WebsocketHandler.websocket.onopen = ev => {
            console.log("opened")
            console.log(ev);
        };

        WebsocketHandler.websocket.onmessage = ev => {
            console.log("message")
            console.log(ev.data);

            WebsocketHandler.app.handleNotification(JSON.parse(ev.data));

        };

        WebsocketHandler.websocket.onerror = ev => {


        };

        WebsocketHandler.websocket.onclose = ev => {


        };

    }


    private static getRandomNumber() {
        return Math.floor(Math.random() * 100000000000000000);
    }

    private static getId(): string {

        let id = LocaleStorageFacade.getUserId();

        if (id === undefined || id === null) {
            id = WebsocketHandler.getRandomNumber() + "";
            localStorage.setItem("user-id", id);
        }
        return id;
    }

    public static createLobby(lobbyName: string | undefined) {
        WebsocketHandler.websocket.send(JSON.stringify(
            {
                command: "createLobby",
                userId: LocaleStorageFacade.getUserId(),
                data: {
                    lobbyName: lobbyName
                }
            }
        ));
    }

    public static joinLobby(lobbyId: string | undefined) {
        WebsocketHandler.websocket.send(JSON.stringify(
            {
                command: "joinLobby",
                userId: LocaleStorageFacade.getUserId(),
                data: {
                    lobbyId: lobbyId
                }
            }
        ));
    }

    public static startLobby(lobbyId: string | undefined): void {
        WebsocketHandler.websocket.send(JSON.stringify(
            {
                command: "startLobby",
                userId: LocaleStorageFacade.getUserId(),
                data: {
                    lobbyId: lobbyId
                }
            }
        ));
    }

    public static playCard(cardId: string) {
        WebsocketHandler.websocket.send(JSON.stringify(
            {
                command: "playCard",
                userId: LocaleStorageFacade.getUserId(),
                data: {
                    cardId: cardId
                }
            }
        ));
    }

    static drawCard() {
        WebsocketHandler.websocket.send(JSON.stringify(
            {
                command: "drawCard",
                userId: LocaleStorageFacade.getUserId(),
            }
        ));
    }
}

export default WebsocketHandler;