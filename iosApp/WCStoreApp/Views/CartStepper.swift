//
//  CartStepper.swift
//  WCStoreApp
//
//  Created by Hicham on 14/9/2022.
//

import SwiftUI

struct CartStepper: View {
    let count: Int
    let addClick: () -> Void
    let deleteClick: () -> Void
    
    var body: some View {
        HStack {
            Button(action: deleteClick) {
                Image(systemName: "minus.circle")
            }
            .disabled(count == 0)
            
            Text(count.formatted())
            
            Button(action: addClick) {
                Image(systemName: "plus.circle")
            }
        }
    }
}

struct CartStepper_Previews: PreviewProvider {
    @State static var count = 0
    static var previews: some View {
        CartStepper(
            count: count,
            addClick: { count = count + 1 },
            deleteClick: { count = count - 1}
        )
    }
}
