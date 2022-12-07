//
// Created by Hicham on 6/12/2022.
//

import Foundation
import WCStoreAppKmm
import KMPNativeCoroutinesCore

class PagingObservable<T: AnyObject>: ObservableObject {
    private let pagingHelper: PagingHelper<T>
    private var cancellationHandle: Task<Void, Error>? = nil

    @Published var count: Int = 0
    @Published var loadStates: Paging_commonCombinedLoadStates

    init(flow: Kotlinx_coroutines_coreFlow) {
        pagingHelper = PagingHelper<T>(flow: flow)
        loadStates = pagingHelper.loadStatesNativeValue

        createFlowPublisher(from: pagingHelper.itemSnapshotListNative, initialValue: pagingHelper.itemSnapshotListNativeValue)
                .map { value in
                    value.count
                }
                .assign(to: &$count)

        createFlowPublisher(from: pagingHelper.loadStatesNative)
                .assign(to: &$loadStates)

        cancellationHandle = Task { @MainActor in
            do {
                try await pagingHelper.collect()
            } catch {
                print("Failed with error: \(error)")
            }
        }
    }

    func retry() {
        pagingHelper.retry()
    }

    func refresh() {
        pagingHelper.refresh()
    }

    func get(position: Int) -> T? {
        pagingHelper.get(index: Int32(position))
    }

    func peek(position: Int) -> T? {
        pagingHelper.peek(index: Int32(position))
    }

    deinit {
        cancellationHandle?.cancel()
    }
}
