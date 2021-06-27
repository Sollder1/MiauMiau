import React from 'react';
import WebsocketHandler from "./websocket/WebsocketHandler";
import {
    ChatMessageNotification,
    GameUpdateNotification,
    IssueNotification,
    LobbyNotification,
    Notification
} from "./datamodel/Notification";
import LocaleStorageFacade from "./common/LocaleStorageFacade";
import {Player} from "./datamodel/GameState";
import CardPictureAccessor from "./common/CardPictureAccessor";
import {
    AppBar,
    Button,
    Container, Divider, Grid,
    List, ListItem, ListItemText,
    Paper, Snackbar,
    TextField,
    Toolbar,
    Typography,
} from "@material-ui/core";
import MuiAlert from '@material-ui/lab/Alert';
import "./App.css";
import ChatMessage from "./datamodel/ChatMessage";


interface Props {

}

enum Page {
    CONNECT_SCREEN,
    JOIN_SCREEN,
    LOBBY_SCREEN,
    GAME_SCREEN
}

interface State {
    username: string,
    lobbyName?: string,
    lobbyId?: string,
    players: Player[],
    currentRenderState: Page,
    myCards?: string[],
    lastCardOnStack?: string,
    currentPlayerId?: string,
    errorOpen: boolean,
    errorText?: string,
    chatMessages: ChatMessage[],
    currentlyTypingTextMessage?: string
}

class App extends React.Component<Props, State> {


    private spacing_default = {marginBottom: "20px"};
    private padding = {padding: "20px"};

    private card = {
        borderRadius: 10
    }

    constructor(props: Props) {
        super(props);

        this.state = {
            username: "",
            currentRenderState: Page.CONNECT_SCREEN,
            players: [],
            errorOpen: false,
            chatMessages: []
        }

    }

    render() {
        return <>
            <Container maxWidth={false}>
                <AppBar color='primary' position="relative" variant="outlined" style={this.spacing_default}>
                    <Toolbar>
                        <Typography variant="h4">
                            Miau-Miau ({this.state.username})
                        </Typography>
                    </Toolbar>
                </AppBar>

                {this.renderStateDependent()}
                <Snackbar open={this.state.errorOpen} autoHideDuration={1000}
                          onClose={() => this.setState({errorOpen: !this.state.errorOpen})}>
                    <MuiAlert elevation={6} variant="filled" severity="warning">{this.state.errorText}</MuiAlert>
                </Snackbar>
            </Container>
        </>;

    }

    private renderStateDependent() {
        switch (this.state.currentRenderState) {
            case Page.CONNECT_SCREEN:
            case Page.JOIN_SCREEN:
                return this.renderConnectScreen();
            case Page.LOBBY_SCREEN:
                return this.renderLobbyScreen();
            case Page.GAME_SCREEN:
                return this.renderGameScreenFrame();
            default:
                return <h1>Unknown State!</h1>
        }
    }

    private renderConnectScreen() {
        return (
            <>
                <Paper elevation={1} style={this.padding}>
                    <TextField
                        disabled={this.state.currentRenderState === Page.JOIN_SCREEN}
                        size="medium"
                        fullWidth
                        onChange={event => this.setState({username: event.target.value})}
                        value={this.state.username}
                        label="Benutzername" variant="outlined"
                        onKeyPress={event => {
                            if (event.key === "Enter") {
                                WebsocketHandler.connect(this.state.username, this)
                                this.setState({currentRenderState: Page.JOIN_SCREEN});
                            }
                        }}
                        style={this.spacing_default}
                    />

                    {
                        this.state.currentRenderState === Page.JOIN_SCREEN ?
                            <>
                                <Typography variant="h6">Lobby erstellen</Typography>
                                <TextField
                                    size="medium"
                                    fullWidth
                                    onChange={event => this.setState({lobbyName: event.target.value})}
                                    value={this.state.lobbyName}
                                    label="Lobby-Name" variant="outlined"
                                    onKeyPress={event => {
                                        if (event.key === "Enter") {
                                            WebsocketHandler.createLobby(this.state?.lobbyName)
                                        }
                                    }}
                                    style={this.spacing_default}
                                />

                                <Typography variant="h6">Lobby beitreten</Typography>

                                <TextField
                                    size="medium"
                                    fullWidth
                                    onChange={event => this.setState({lobbyId: event.target.value})}
                                    value={this.state.lobbyId}
                                    label="Lobby-Id" variant="outlined"
                                    onKeyPress={event => {
                                        if (event.key === "Enter") {
                                            WebsocketHandler.joinLobby(this.state?.lobbyId);
                                        }
                                    }}
                                    style={this.spacing_default}
                                />
                            </>
                            : null
                    }
                </Paper>
            </>
        );
    }


    private renderLobbyScreen() {
        return (
            <Paper elevation={3} style={this.padding}>

                <Typography variant="h5" style={this.spacing_default}>{this.state.lobbyName}</Typography>

                {this.renderPlayerList()}
                <TextField
                    disabled={true}
                    defaultValue={this.state.lobbyId}
                    label="Lobby-Id" variant="outlined"
                    fullWidth
                    style={this.spacing_default}
                />

                <Button
                    onClick={() => WebsocketHandler.startLobby(this.state.lobbyId)}
                    variant="contained"
                >
                    Spiel starten
                </Button>

            </Paper>
        );
    }


    private renderGameScreenFrame() {
        return (
            <>
                <Paper elevation={1} style={this.padding}>
                    <Typography variant="h4" style={this.spacing_default}>{this.state.lobbyName}</Typography>
                    <Grid container spacing={3}>
                        <Grid item xs={12} sm={12} md={12} lg={3} xl={4}>
                            {this.renderPlayerList()}
                            {this.renderMessagesContainer()}
                        </Grid>
                        <Grid item xs={12} sm={12} md={12} lg={9} xl={8}>
                            {this.renderGameScreen()}
                        </Grid>
                    </Grid>
                </Paper>
            </>
        );
    }

    private renderGameScreen() {
        return <>
            <Paper style={this.padding} elevation={3}>
                <Grid
                    container
                    direction="row"
                    justify="center"
                    alignItems="center"
                    style={this.spacing_default}
                >
                    <Paper elevation={10}>
                        <img key={"stack_img"}
                             src={CardPictureAccessor.getLinkByName(this.state.lastCardOnStack)}
                             alt={this.state.lastCardOnStack}
                             width={"200px"}
                        />
                    </Paper>
                </Grid>

                <Grid
                    container
                    direction="row"
                    justify="center"
                    alignItems="center"
                    style={this.spacing_default}
                >
                    <Button
                        size="large"
                        variant="contained"
                        onClick={() => WebsocketHandler.drawCard()}
                        style={this.spacing_default}
                    >
                        Karte ziehen
                    </Button>
                </Grid>

            </Paper>
            <br/>
            <Paper elevation={3}>
                <Grid
                    container
                    direction="row"
                    justify="center"
                    alignItems="center"
                >

                    {this.state.myCards?.map(value =>
                        <Paper elevation={10} className="mau-mau-card">
                            <img key={value + "_img"}
                                 src={CardPictureAccessor.getLinkByName(value)}
                                 alt={value}
                                 width={"200px"}
                                 onClick={() => WebsocketHandler.playCard(value)}
                                 style={this.card}
                            />
                        </Paper>
                    )}
                </Grid>
            </Paper>
        </>;
    }

    private renderMessagesContainer() {
        return <Paper style={{...this.padding, ...this.spacing_default}} elevation={3}>
            <Typography variant="h6" style={this.spacing_default}>Nachrichten</Typography>

            <List dense>
                {this.state.chatMessages?.map(value => {
                    return <>
                        <ListItem key={value.id}>
                            <ListItemText
                                primary={<b>{value.username} ({value.time})</b>}
                                secondary={value.text}
                            />
                        </ListItem>
                    </>
                })}
            </List>

            <TextField
                disabled={false}
                value={this.state.currentlyTypingTextMessage}
                onChange={event => this.setState({currentlyTypingTextMessage: event.target.value})}
                label="Nachricht schreiben" variant="outlined"
                fullWidth
                style={this.spacing_default}
                onKeyPress={event => {
                    if (event.key === "Enter") {
                        WebsocketHandler.sendMessage(this.state?.currentlyTypingTextMessage);
                        this.setState({currentlyTypingTextMessage: ""});
                    }
                }}
            />

        </Paper>;
    }

    private renderPlayerList() {
        return <Paper style={{...this.padding, ...this.spacing_default}} elevation={3}>
            <Typography variant="h6" style={this.spacing_default}>Spieler</Typography>

            <List dense>
                {this.state.players.map(value => {
                    return <>
                        <ListItem key={value.playerId}><ListItemText primary={<b>{value.playerName}</b>} secondary={
                            value.playerId === this.state.currentPlayerId ? "dran" : null
                        }/></ListItem>
                        <Divider/>
                    </>
                })}
            </List>
        </Paper>;
    }

    handleNotification(notification: Notification) {
        debugger;
        switch (notification.event) {
            case "joinedLobby":
                return this.handleJoinLobby(notification as LobbyNotification);
            case "leftLobby":
                return this.handleLeftLobby(notification as LobbyNotification);
            case "gameUpdate":
                return this.handleGameUpdate(notification as GameUpdateNotification);
            case "issue":
                return this.handleIssue(notification as IssueNotification);
            case "newMessage":
                return this.handleNewMessage(notification as ChatMessageNotification);
            default:
                break;
        }
    }

    private handleJoinLobby(notification: LobbyNotification) {
        const list = this.state.players.concat([{
            playerId: notification.playerId,
            playerName: notification.playerName
        }]);

        if (this.state.currentRenderState === Page.JOIN_SCREEN && notification.playerId === LocaleStorageFacade.getUserId()) {
            this.setState({
                lobbyName: notification.lobbyName,
                lobbyId: notification.lobbyId,
                currentRenderState: Page.LOBBY_SCREEN,
                players: list
            });
        } else {
            this.setState({players: list});
        }
    }


    private handleLeftLobby(notification: LobbyNotification) {
        const newArray = this.state.players.filter(
            (player: Player) => player.playerId !== notification.playerId
        );
        this.setState({players: newArray});
    }


    private handleGameUpdate(notification: GameUpdateNotification) {
        if (notification.lobbyStarted && this.state.currentRenderState !== Page.GAME_SCREEN) {
            this.setState({currentRenderState: Page.GAME_SCREEN});
        }
        this.setState({
            lastCardOnStack: notification.lastCardOnStack,
            myCards: notification.myCards,
            currentPlayerId: notification.currentPlayerId
        })
    }

    private handleIssue(notification: IssueNotification) {
        this.setState({errorOpen: true, errorText: notification.text});
    }

    private handleNewMessage(notification: ChatMessageNotification) {
        const list = this.state.chatMessages.concat([notification]);
        this.setState({chatMessages: list});
    }
}

export default App;