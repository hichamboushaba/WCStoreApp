//
//  OrderPlacedScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 16/9/2022.
//

import SwiftUI
import WCStoreAppKmm

struct OrderPlacedScreen: View {
    private let navigationManager: NavigationManager = Koin.getProtocol(NavigationManager.self)
    let orderId: String
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            VStack {
                Text("Thank you for your order")
                Text("Your Order ID: \(orderId)")
                
                Spacer().frame(height: 32)
                
                Button(action: {navigationManager.popUpTo(route: KMMScreen.Home.shared.route, inclusive: false)}) {
                    Text("Continue Shopping")
                }
            }
        }
    }
}
