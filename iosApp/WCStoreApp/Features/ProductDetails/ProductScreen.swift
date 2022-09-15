//
//  ProductScreen.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 13/3/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

struct ProductScreen: View {
    private let viewModel: ProductViewModel
    @FlowWrapper private var uiState: ProductViewModel.UiState
    
    init(productId: Int64) {
        viewModel = KoinKt.get(objCClass: ProductViewModel.self, parameters: [productId]) as! ProductViewModel
        _uiState = FlowWrapper(viewModel.uiStateNative, initialValue: viewModel.uiStateNativeValue)
    }
    
    var body: some View {
        Screen() {
            if (uiState is ProductViewModel.UiStateLoadingState) {
                ProgressView()
            } else if (uiState is ProductViewModel.UiStateSuccessState) {
                let state = uiState as! ProductViewModel.UiStateSuccessState
                Text(state.product.name)
                    .navigationTitle(state.product.name)
            } else {
                EmptyView()
            }
        }
    }
}

struct ProductScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductScreen(productId: 0)
    }
}
