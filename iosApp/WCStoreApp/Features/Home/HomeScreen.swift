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
    let viewModel = KoinKt.get(objCClass: HomeViewModel.self) as! HomeViewModel
    @FlowWrapper var productsState: ProductsUiListState
    
    init() {
        _productsState = FlowWrapper(
            viewModel.productsNative,
            initialValue: ProductsUiListState.init(products: [], hasNext: true, state: LoadingState.loading)
        )
    }

    
    var body: some View {
        List {
            ForEach(productsState.products, id: \.product.id) { productUiModel in
                ProductsListRowView(uiModel: productUiModel, onClick: {
                    viewModel.onProductClicked(product: productUiModel.product)
                })
            }
            if productsState.hasNext {
                nextPageView
            }
        }
        .navigationTitle("Products")
        .onDisappear(perform: {
            viewModel.close()
        })
    }
    
    private var nextPageView: some View {
        HStack {
            Spacer()
            VStack {
                ProgressView()
            }
            Spacer()
        }
        .onAppear(perform: {
            viewModel.loadNext()
        })
    }
}

struct ProductsListRowView: View {
    let uiModel: ProductUiModel
    let onClick: () -> Void
    
    var body: some View {
        HStack {
            if let image = uiModel.product.images.first,
               let url = URL(string: image) {
                AsyncImage(url: url) { image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 50, height: 50)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            } else {
                RoundedRectangle(cornerRadius: 8)
                    .frame(width: 50, height: 50)
                    .foregroundColor(.gray)
            }
            VStack(alignment: .leading) {
                Text(uiModel.product.name )
                    .font(.title3)
                    .foregroundColor(.accentColor)
                Text(uiModel.priceFormatted)
                    .font(.footnote)
                    .foregroundColor(.gray)
            }
        }
        .onTapGesture(perform: onClick)
    }
}


//struct ProductsList_Previews: PreviewProvider {
//    static var previews: some View {
//        ProductsList()
//    }
//}
