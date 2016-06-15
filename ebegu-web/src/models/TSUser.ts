import {TSRole} from './enums/TSRole';

export default class TSUser {

    private _userId: string;
    private _nachname: string;
    private _vorname: string;
    private _username: string;
    private _password: string;
    private _email: string;
    private _roles: Array<TSRole>;

    constructor(userId?: string, nachname?: string, vorname?: string, username?: string,
                password?: string, email?: string, roles?: Array<TSRole>) {
        this._userId = userId;
        this._nachname = nachname;
        this._vorname = vorname;
        this._username = username;
        this._password = password;
        this._email = email;
        this._roles = roles;
    }


    get userId(): string {
        return this._userId;
    }

    set userId(value: string) {
        this._userId = value;
    }

    get nachname(): string {
        return this._nachname;
    }

    set nachname(value: string) {
        this._nachname = value;
    }

    get vorname(): string {
        return this._vorname;
    }

    set vorname(value: string) {
        this._vorname = value;
    }

    get username(): string {
        return this._username;
    }

    set username(value: string) {
        this._username = value;
    }

    get password(): string {
        return this._password;
    }

    set password(value: string) {
        this._password = value;
    }

    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get roles(): Array<TSRole> {
        return this._roles;
    }

    set roles(value: Array<TSRole>) {
        this._roles = value;
    }
}
