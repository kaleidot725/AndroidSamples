import SwiftUI
import shared

let counter = MyCounter(min: 0, max: 100)

struct ContentView: View {
    @State var count = counter.value
    
    var body: some View {
        
        VStack {
            Text(String(count))
            
            Button(action: {
                counter.plus()
                count = counter.value
            }) {
                Text("PLUS")
            }
            
            Button(action: {
                counter.minus()
                count = counter.value
            }) {
                Text("MINUS")
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
	ContentView()
	}
}
