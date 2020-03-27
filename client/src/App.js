import React from 'react';
import './App.css';
import { Table, TableHead, TableRow, TableCell, TableBody, Button, Switch, FormControlLabel } from '@material-ui/core';

class App extends React.Component {

  state = {
    positiveListings: {},
    negativeListings: {},
    neutralListings: {}
  }

  componentDidMount() {
    fetch('http://localhost:8080/api/locations/').then((res => res.json())).then((results) => {
      const positiveListings = {}
      const negativeListings = {}
      const neutralListings = {}
      results.forEach((result) => {
        if (result.ls_name.split(' ')[1] === "Positive") {
          positiveListings[result.ls_name.split(' ')[0]] = result.ls_count;
        } else if (result.ls_name.split(' ')[1] === "Negative") {
          positiveListings[result.ls_name.split(' ')[0]] = result.ls_count;
        } else {
          positiveListings[result.ls_name.split(' ')[0]] = result.ls_count;
        }
      })
      this.setState({ positiveListings, negativeListings, neutralListings })
    });
  }

  getPositiveSentiments() {
    let arr = []
    Object.keys(this.state.positiveListings).forEach((key, index, map) => {
      arr.push({ location: key, count: this.state.positiveListings[key] })
    })
    console.log(arr)
    arr.map((row, index) => {
      return (
        <TableRow key={row.ls_name}>
          <TableCell align="center">{index + 1}</TableCell>
          <TableCell align="center" component="th" scope="row">
            {row.location}
          </TableCell>
          <TableCell align="center">{row.count}</TableCell>
        </TableRow>
      )
    })
  }

  getNegativeSentiments() {
    console.log(this.state.negativeListings)
    let arr = []
    Object.keys(this.state.negativeListings).forEach((key, index, map) => {
      console.log(map)
      arr.push({ location: key, count: this.state.negativeListings[key] })
    })

    arr.forEach((row, counter) => {
      return (
        <TableRow key={row.ls_name}>
          <TableCell align="center">{counter + 1}</TableCell>
          <TableCell align="center" component="th" scope="row">
            {row.location}
          </TableCell>
          <TableCell align="center">{row.count}</TableCell>
        </TableRow>
      )
    })
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
                  // checked={}
                  // onChange={}
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
                {/* {this.getPositiveSentiments()} */}
                {Object.keys(this.state.negativeListings).forEach((key, index, map) => (
                  <TableRow key={key.ls_name}>
                    <TableCell align="center">{index + 1}</TableCell>
                    <TableCell align="center" component="th" scope="row">
                      {key.location}
                    </TableCell>
                    <TableCell align="center">{key.count}</TableCell>
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
                  // checked={}
                  // onChange={}
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
              {this.getNegativeSentiments()}
                {/* {this.getNegativeSentiments().forEach((row, counter) => (
                  <TableRow key={row.ls_name}>
                    <TableCell align="center">{counter + 1}</TableCell>
                    <TableCell align="center" component="th" scope="row">
                      {row.location}
                    </TableCell>
                    <TableCell align="center">{row.count}</TableCell>
                  </TableRow>
                ))}} */}
              </TableBody>
            </Table>
          </div>
        </header>
      </div>
    );
  }
}

export default App;
