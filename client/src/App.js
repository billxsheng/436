import React from 'react';
import './App.css';
import { Table, TableHead, TableRow, TableCell, TableBody, Switch, FormControlLabel } from '@material-ui/core';

class App extends React.Component {

  state = {
    positiveListings: [],
    negativeListings: [],
    pneuListings: [],
    nneuListings: [],
    showPNeutral: false,
    showNNeutral: false,
  }

  componentDidMount() {
    fetch('http://localhost:8080/api/locations/').then((res => res.json())).then((results) => {
      let positiveListingsMap = {}
      let negativeListingsMap = {}
      let neutralListingsMap = {}
      results.forEach((result) => {
        if (result.ls_name.split(' ')[1] === "Positive") {
          positiveListingsMap[result.ls_name.split(' ')[0]] = result.ls_count;
        } else if (result.ls_name.split(' ')[1] === "Negative") {
          negativeListingsMap[result.ls_name.split(' ')[0]] = result.ls_count;
        } else {
          neutralListingsMap[result.ls_name.split(' ')[0]] = result.ls_count;
        }
      })

      let pneuListingsMap = this.sumObjectsByKey(positiveListingsMap, neutralListingsMap);
      let nneuListingsmap = this.sumObjectsByKey(negativeListingsMap, neutralListingsMap);

      let positiveListings = this.convertMapToSortedList(positiveListingsMap)
      let negativeListings = this.convertMapToSortedList(negativeListingsMap)
      let pneuListings = this.convertMapToSortedList(pneuListingsMap)
      let nneuListings = this.convertMapToSortedList(nneuListingsmap)

      this.setState({ positiveListings, negativeListings, pneuListings, nneuListings })
    });
  }

  convertMapToSortedList = (map) => {
    let list = [];
    for(let entry in map) {
      list.push({location: entry, count: map[entry]});
    }
    list.sort((a,b) => {
      return b.count - a.count;
    });

    return list;
  }

  sumObjectsByKey = (...objs) => {
    return objs.reduce((a, b) => {
      for (let k in b) {
        if (b.hasOwnProperty(k))
          a[k] = (a[k] || 0) + b[k];
      }
      return a;
    }, {});
  }

  handlePositiveSwitch = () => {
    this.setState(prevState => ({
      showPNeutral: !prevState.showPNeutral
    }))
  }

  handleNegativeSwitch = () => {
    this.setState(prevState => ({
      showNNeutral: !prevState.showNNeutral
    }))
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1>Tour Decision Support System</h1>
          <div style={{ float: 'left', width: '500px', marginLeft: '100px' }}>
            <p>Most Positive Locations</p>
            <FormControlLabel
              control={
                <Switch
                  checked={this.state.showPNeutral}
                  onChange={this.handlePositiveSwitch}
                  name="checkedB"
                  color="primary"
                />
              }
              label="Include Neutral"
            />
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell align="center">Rank</TableCell>
                  <TableCell align="center">Location</TableCell>
                  <TableCell align="center">Count</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {this.state.showPNeutral ?
                this.state.pneuListings.map((row, index) => (
                  <TableRow key={index}>
                    <TableCell align="center">{index + 1}</TableCell>
                    <TableCell align="center" component="th" scope="row">
                      {row.location}
                    </TableCell>
                    <TableCell align="center">{row.count}</TableCell>
                  </TableRow>
                ))
                :
                this.state.positiveListings.map((row, index) => (
                  <TableRow key={index}>
                    <TableCell align="center">{index + 1}</TableCell>
                    <TableCell align="center" component="th" scope="row">
                      {row.location}
                    </TableCell>
                    <TableCell align="center">{row.count}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          <div style={{ float: 'right', width: '500px', marginRight: '100px' }}>
            <p>Most Negative Locations</p>
            <FormControlLabel
              control={
                <Switch
                  checked={this.state.showNNeutral}
                  onChange={this.handleNegativeSwitch}
                  name="checkedB"
                  color="primary"
                />
              }
              label="Include Neutral"
            />
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell align="center">Rank</TableCell>
                  <TableCell align="center">Location</TableCell>
                  <TableCell align="center">Count</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {this.state.showNNeutral ?
                this.state.nneuListings.map((row, index) => (
                  <TableRow key={index}>
                    <TableCell align="center">{index + 1}</TableCell>
                    <TableCell align="center" component="th" scope="row">
                      {row.location}
                    </TableCell>
                    <TableCell align="center">{row.count}</TableCell>
                  </TableRow>
                ))
                :
                this.state.negativeListings.map((row, index) => (
                  <TableRow key={index}>
                    <TableCell align="center">{index + 1}</TableCell>
                    <TableCell align="center" component="th" scope="row">
                      {row.location}
                    </TableCell>
                    <TableCell align="center">{row.count}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </header>
      </div>
    );
  }
}

export default App;
