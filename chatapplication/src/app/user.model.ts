export class User{
    public type: string;
    public lname: string;
    public sender: string;

    constructor(jsonStr: string) {
        let jsonObj = JSON.parse(jsonStr);
        for (let prop in jsonObj) {
            this[prop] = jsonObj[prop];
        }
    }
}