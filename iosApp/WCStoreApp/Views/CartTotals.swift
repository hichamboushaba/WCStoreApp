//
//  CartTotals.swift
//  WCStoreApp
//
//  Created by Hicham on 16/9/2022.
//

import SwiftUI

struct CartTotals: View {
    let subtotal: String
    let tax: String
    let shippingCost: String? = nil
    let total: String
    let buttonLabel: String
    let buttonEnabled: Bool = true
    let onButtonClick: () -> Void
    
    var body: some View {
        VStack {
            Text("Cart Totals").font(.title3).padding(.bottom).frame(maxWidth: .infinity, alignment: .leading)
            HStack {
                Text("Subtotal").font(.headline)
                Spacer()
                Text(subtotal).font(.subheadline)
            }
            HStack {
                Text("Tax").font(.headline)
                Spacer()
                Text(tax).font(.subheadline)
            }.padding(.bottom)
            if let shippingCost = shippingCost {
                HStack {
                    Text("Shipping").font(.headline)
                    Spacer()
                    Text(shippingCost).font(.subheadline)
                }
            }
            Spacer().frame(height: 8)
            HStack {
                Text("Total").font(.headline).fontWeight(Font.Weight.bold)
                Spacer()
                Text(total).font(.subheadline).fontWeight(Font.Weight.bold)
            }.padding(.bottom)
            
            Button(action: onButtonClick){
                Text(buttonLabel)
            }
            .disabled(!buttonEnabled)
        }.frame(maxWidth:.infinity)
            .padding()
    }
}
