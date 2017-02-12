class Card extends React.Component {

    render() {

        var className = "card "
        if (this.props.loading) {
            className = "card waitMe_container"
        }

        var top = (<span></span>)

        if (this.props.top == true) {
            top=(<div className="header">
                <h2>
                    {this.props.header}
                </h2>
                <ul className="header-dropdown m-r--5">
                    <li>
                        <a href="javascript:void(0);" data-toggle="cardloading" data-loading-effect="timer">
                            <i className="material-icons">loop</i>
                        </a>
                    </li>
                    <li className="dropdown">
                        <a href="javascript:void(0);" className="dropdown-toggle" data-toggle="dropdown"
                           role="button" aria-haspopup="true" aria-expanded="false">
                            <i className="material-icons">more_vert</i>
                        </a>
                        <ul className="dropdown-menu pull-right">
                            <li><a href="javascript:void(0);" className=" waves-effect waves-block">Action</a>
                            </li>
                            <li><a href="javascript:void(0);" className=" waves-effect waves-block">Another
                                action</a></li>
                            <li><a href="javascript:void(0);" className=" waves-effect waves-block">Something
                                else here</a></li>
                        </ul>
                    </li>
                </ul>
            </div>)
        }

        var width = this.props.width || "4"
        var height = this.props.height || "auto"

        if (height != "auto") height = height + "px"

        return (
            <div className={"col-lg-" + width + " col-md-4 col-sm-6 col-xs-12"}>
                <div className={className}>
                    {top}
                    <div className="body" style={{"height": height, "overflow-y": "auto"}}>
                        {this.props.children}
                    </div>
                </div>
            </div>
        )
    }
}


var App = React.createClass({
    setGreeting: function () {
        this.setState({
            name: this.state.new_name
        });
    },
    componentDidMount: function () {

    },
    getInitialState: function () {
        return {
            name: 'enter your name',
            scanList: [],
            user: {}
        }
    },
    setName: function (event) {
        this.setState({
            new_name: event.target.value.toLowerCase(),
            name: this.state.name
        });
    },
    render: function () {
        return (
            <div class="my-component">
            <Card />
            <Card />
            <Card />
            <Card />

        <h1>Hello, {this.state.name}</h1>
        <input type="text" placeholder="Name" onChange={this.setName}/>

        <button type="button" onClick={this.setGreeting}>submit</button>
        </div>
        )
    }
});

ReactDOM.render(
    <App />,
    document.getElementById('page-body')
);
