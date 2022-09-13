//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import SwiftUI
import Combine
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

struct HomeScreen: View {
    private let viewModel = KoinKt.get(objCClass: HomeViewModel.self) as! HomeViewModel
    @FlowWrapper private var productsState: ProductsUiListState
    
    init() {
        _productsState = FlowWrapper(
            viewModel.productsNative,
            initialValue: ProductsUiListState.init(products: [], hasNext: true, state: LoadingState.loading)
        )
    }

    
    var body: some View {
        Screen(hasNavigationBar: false, viewModel: viewModel) {
            ProductsList(productsState: productsState, onProductClick: viewModel.onProductClicked, loadNext: viewModel.loadNext)
        }
    }
}
