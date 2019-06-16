
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Button} from 'react-native';



type Props = {};
type State = { info: string }
export default class App extends Component<Props,State> {



  constructor(props : Props) {
    super(props);
     
    this.state = {
      info: "Zgadnij liczbe miedzy 1 a 20",
      number: 0,
      guessedNumber : 10,
    };
    this.generateRandom();
    
  }

  generateRandom(){
    const min = 1;
    const max = 20;
    const rand =  min + Math.floor(Math.random() * (max - min));
    this.state.number = rand
  }

  decrease() {
    var num = this.state.guessedNumber - 1;
    this.setState({guessedNumber: num})
  }

  
  increase() {
    var num = this.state.guessedNumber + 1;
    this.setState({guessedNumber: num})
  
  }

  guess() {
    if(this.state.number === this.state.guessedNumber){
      var info = "Wygrałeś, zgadywany numer to " + this.state.number;
      this.setState({info: info});
    }
    else if(  this.state.number > this.state.guessedNumber){
      this.setState({info: "Zgadywany numer jest wiekszy od podanego"});
    }
    else{
      this.setState({info: "Zgadywany numer jest mniejszy od podanego"});
    }
  }
  reset(){
    this.state = {
      info: "Zgadnij liczbe miedzy 1 a 20",
      number: 0,
      guessedNumber : 10,
    };
    this.setState({info: "Zgadnij liczbe miedzy 1 a 20"})
    this.setState({guessedNumber: 10})
    const min = 1;
    const max = 20;
    const rand =  min + Math.floor(Math.random() * (max - min));
    this.setState({number: rand})
  }

  render() {
    return (
      <View style={styles.container}>
      <Text style={styles.instructions}>{this.state.info}</Text>
      <Text style={styles.instructions}>Twoj nr: {this.state.guessedNumber}</Text>
        <View style={styles.horiz}>
          <Button title="Zmniejsz" onPress={this.decrease.bind(this)}/>
          <Button title="Zgadnij" onPress={this.guess.bind(this)}/>
          <Button title="Zwiększ" onPress={this.increase.bind(this)}/>
         
        </View>
        <View style={styles.horiz}>
          <Button title="Resetuj" onPress={this.reset.bind(this)}/>
         
        </View>
        
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  horiz: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "100%",
    padding: 10,
  },

  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
